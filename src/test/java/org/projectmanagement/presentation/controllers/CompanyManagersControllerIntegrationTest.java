package org.projectmanagement.presentation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.projectmanagement.application.dto.company_managers.CreateCompanyManagers;
import org.projectmanagement.application.dto.users.UsersLogin;
import org.projectmanagement.domain.entities.Companies;
import org.projectmanagement.domain.entities.CompanyManagers;
import org.projectmanagement.domain.entities.Roles;
import org.projectmanagement.domain.entities.Users;
import org.projectmanagement.domain.repository.CompanyManagersJpaRepo;
import org.projectmanagement.domain.repository.RolesRepoJpa;
import org.projectmanagement.domain.repository.UsersRepoJpa;
import org.projectmanagement.domain.repository.jpa.CompaniesJpaRepository;
import org.projectmanagement.domain.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class CompanyManagersControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CompanyManagersJpaRepo companyManagersJpaRepo;

    @Autowired
    private CompaniesJpaRepository companiesJpaRepository;

    @Autowired
    private UsersRepoJpa usersRepoJpa;

    @Autowired
    private RolesRepoJpa rolesRepoJpa;

    @Autowired
    private AuthService authService;

    UUID companyId;
    UUID roleAdminId;
    UUID user1Id;
    UUID user2Id;
    UUID companyManagerId;


    @BeforeEach
    void setUp() {
        companyManagersJpaRepo.deleteAll();
        usersRepoJpa.deleteAll();
        rolesRepoJpa.deleteAll();
        companiesJpaRepository.deleteAll();

        Companies company = companiesJpaRepository.save(Companies.builder()
                .id(companyId)
                .name("Test Company")
                .build());

        companyId = company.getId();

        Roles role = rolesRepoJpa.save(Roles.builder()
                .name("ADMIN")
                .companyId(companyId)
                .isDeleted(false)
                .isSystemRole(true)
                .build());

        roleAdminId = role.getId();

        Users user1 = usersRepoJpa.save(Users.builder()
                .name("user1")
                .email("email")
                .passwordHash("asdf")
                .isActive(true)
                .isDeleted(false)
                .isOwner(true)
                .companyId(companyId)
                .build());
        user1Id = user1.getId();

        Users user2 = usersRepoJpa.save(Users.builder()
                .name("user2")
                .email("email2")
                .passwordHash("asdf")
                .isActive(true)
                .isDeleted(false)
                .isOwner(true)
                .companyId(companyId)
                .build());

        user2Id = user2.getId();

        CompanyManagers companyManager = companyManagersJpaRepo.save(CompanyManagers.builder()
                .userId(user1Id)
                .roleId(roleAdminId)
                .companyId(companyId)
                .build());

        companyManagerId = companyManager.getId();


        companyManagersJpaRepo.save(CompanyManagers.builder()
                .userId(user2Id)
                .roleId(roleAdminId)
                .companyId(companyId)
                .build());
    }

    @Nested
    class GetCompanyManagers {
        @Test
        void getAllCorrectly() throws Exception {
            UsersLogin userLogin = new UsersLogin("email", "asdf");

            String token = authService.authenticate(userLogin);

            mockMvc.perform(get("/"+ companyId + "/companyManagers")
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data", hasSize(2)))
                    .andDo(print());
        }

        @Test
        void getOneById() throws Exception {
            UsersLogin userLogin = new UsersLogin("email", "asdf");

            String token = authService.authenticate(userLogin);

            mockMvc.perform(get("/"+ companyId + "/companyManagers/" + companyManagerId)
                    .header("Authorization", "Bearer " + token))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.data").isMap())
                    .andExpect(jsonPath("$.data.id").value(companyManagerId.toString()))
                    .andDo(print());
        }
    }

    @Nested
    class PostRoles {
        @Test
        void shouldCreateCompanyManagerNormallyWithProperData() throws Exception{
            companyManagersJpaRepo.deleteAll();
            UsersLogin userLogin = new UsersLogin("email", "asdf");

            String token = authService.authenticate(userLogin);

            CreateCompanyManagers companyManagers = new CreateCompanyManagers(user1Id, companyId, roleAdminId);
            String roleJson = objectMapper.writeValueAsString(companyManagers);

            mockMvc.perform(post("/" + companyId + "/companyManagers")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(roleJson))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.data.roleId").value(roleAdminId.toString()))
                    .andDo(print());
        }

        @Test
        void shouldNotCreateCompanyManagerWithNonExistingUserId() throws Exception{
            companyManagersJpaRepo.deleteAll();
            UUID nonExistingUserId = UUID.fromString("fed18c17-dc47-45f4-8b59-aafc04089a58");
            CreateCompanyManagers companyManagers = new CreateCompanyManagers(nonExistingUserId, companyId, roleAdminId);
            String roleJson = objectMapper.writeValueAsString(companyManagers);

            UsersLogin userLogin = new UsersLogin("email", "asdf");

            String token = authService.authenticate(userLogin);

            mockMvc.perform(post("/" + companyId + "/companyManagers")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(roleJson))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.data").value(nullValue()))
                    .andExpect(jsonPath("$.errors[0].message").value("User with id: " + nonExistingUserId + " is not found"))
                    .andDo(print());
        }

        @Test
        void shouldNotCreateCompanyManagerWithNonExistingRoleIdWithId() throws Exception{
            companyManagersJpaRepo.deleteAll();
            UUID nonExistingRoleId = UUID.fromString("fed18c17-dc47-45f4-8b59-aafc04089a58");
            CreateCompanyManagers companyManagers = new CreateCompanyManagers(user1Id, companyId, nonExistingRoleId);
            String roleJson = objectMapper.writeValueAsString(companyManagers);

            UsersLogin userLogin = new UsersLogin("email", "asdf");

            String token = authService.authenticate(userLogin);

            mockMvc.perform(post("/" + companyId + "/companyManagers")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(roleJson))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.data").value(nullValue()))
                    .andExpect(jsonPath("$.errors[0].message").value("Role with id: " + nonExistingRoleId + " is not found"))
                    .andDo(print());
        }

        @Test
        void shouldNotCreateCompanyManagerWithNonManagerRole() throws Exception{
            companyManagersJpaRepo.deleteAll();

            Roles role = rolesRepoJpa.save(Roles.builder()
                    .name("DEVELOPER")
                    .companyId(companyId)
                    .isDeleted(false)
                    .isSystemRole(true)
                    .build());

            CreateCompanyManagers companyManagers = new CreateCompanyManagers(user1Id, companyId, role.getId());
            String roleJson = objectMapper.writeValueAsString(companyManagers);

            UsersLogin userLogin = new UsersLogin("email", "asdf");

            String token = authService.authenticate(userLogin);

            mockMvc.perform(post("/" + companyId + "/companyManagers")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(roleJson))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.data").value(nullValue()))
                    .andExpect(jsonPath("$.errors[0].message").value("Company Managers can only be assigned to ADMIN or COMPANY_MANAGER roles."))
                    .andDo(print());
        }
    }
}
