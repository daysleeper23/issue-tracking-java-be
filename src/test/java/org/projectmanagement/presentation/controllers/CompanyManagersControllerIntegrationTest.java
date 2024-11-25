package org.projectmanagement.presentation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.projectmanagement.application.dto.company_managers.CreateCompanyManagers;
import org.projectmanagement.application.dto.company_managers.UpdateCompanyManagers;
import org.projectmanagement.application.dto.users.UsersLogin;
import org.projectmanagement.domain.entities.Companies;
import org.projectmanagement.domain.entities.CompanyManagers;
import org.projectmanagement.domain.entities.Roles;
import org.projectmanagement.domain.entities.Users;
import org.projectmanagement.domain.repository.CompanyManagersRepoJpa;
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

import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    private CompanyManagersRepoJpa companyManagersRepoJpa;

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
    UUID roleCompanyManagerId;




    @BeforeEach
    void setUp() {
        companyManagersRepoJpa.deleteAll();
        usersRepoJpa.deleteAll();
        rolesRepoJpa.deleteAll();
        companiesJpaRepository.deleteAll();

        Companies company = companiesJpaRepository.save(Companies.builder()
                .id(companyId)
                .name("Test Company")
                .build());

        companyId = company.getId();

        Roles role = rolesRepoJpa.save(Roles.builder()
                .name("Super Admin")
                .companyId(companyId)
                .isDeleted(false)
                .isSystemRole(true)
                .build());

        roleAdminId = role.getId();

        Roles role2 = rolesRepoJpa.save(Roles.builder()
                .name("Company Manager")
                .companyId(companyId)
                .isDeleted(false)
                .isSystemRole(true)
                .build());

        roleCompanyManagerId = role2.getId();

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

        CompanyManagers companyManager = companyManagersRepoJpa.save(CompanyManagers.builder()
                .userId(user1Id)
                .roleId(roleAdminId)
                .companyId(companyId)
                .build());

        companyManagerId = companyManager.getId();


        companyManagersRepoJpa.save(CompanyManagers.builder()
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
            companyManagersRepoJpa.deleteAll();
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
            companyManagersRepoJpa.deleteAll();
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
            companyManagersRepoJpa.deleteAll();
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
            companyManagersRepoJpa.deleteAll();

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

    @Nested
    class PatchCompanyManagers {
        @Test
        void shouldUpdateCompanyManagerWithProperData() throws Exception {
            UsersLogin userLogin = new UsersLogin("email", "asdf");
            String token = authService.authenticate(userLogin);

            UpdateCompanyManagers updateDto = new UpdateCompanyManagers(roleCompanyManagerId);
            String updateJson = objectMapper.writeValueAsString(updateDto);

            mockMvc.perform(patch("/" + companyId + "/companyManagers/" + companyManagerId)
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(updateJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.data.roleId").value(roleCompanyManagerId.toString()))
                    .andDo(print());
        }

        @Test
        void shouldNotUpdateCompanyManagerWithNonManagerRole() throws Exception {
            UsersLogin userLogin = new UsersLogin("email", "asdf");
            String token = authService.authenticate(userLogin);

            Roles developerRole = rolesRepoJpa.save(Roles.builder()
                    .name("DEVELOPER")
                    .companyId(companyId)
                    .isDeleted(false)
                    .isSystemRole(true)
                    .build());

            UpdateCompanyManagers updateDto = new UpdateCompanyManagers(developerRole.getId());
            String updateJson = objectMapper.writeValueAsString(updateDto);

            mockMvc.perform(patch("/" + companyId + "/companyManagers/" + companyManagerId)
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(updateJson))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.data").value(nullValue()))
                    .andExpect(jsonPath("$.errors[0].message").value("Company Managers can only be assigned to ADMIN or COMPANY_MANAGER roles."))
                    .andDo(print());
        }

        @Test
        void shouldNotUpdateNonExistingCompanyManager() throws Exception {
            UsersLogin userLogin = new UsersLogin("email", "asdf");
            String token = authService.authenticate(userLogin);

            UUID nonExistingManagerId = UUID.fromString("fed18c17-dc47-45f4-8b59-aafc04089a58");
            //UUID validRoleId = rolesRepoJpa.findById(roleAdminId).get().getId();

            UpdateCompanyManagers updateDto = new UpdateCompanyManagers(roleCompanyManagerId);
            String updateJson = objectMapper.writeValueAsString(updateDto);

            mockMvc.perform(patch("/" + companyId + "/companyManagers/" + nonExistingManagerId)
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(updateJson))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.data").value(nullValue()))
                    .andExpect(jsonPath("$.errors[0].message").value("Company Manager with id: " + nonExistingManagerId + "is not found"))
                    .andDo(print());
        }
    }

    @Nested
    class DeleteCompanyManagers {
        @Test
        void shouldDeleteCompanyManagerSuccessfully() throws Exception {
            UsersLogin userLogin = new UsersLogin("email", "asdf");
            String token = authService.authenticate(userLogin);

            mockMvc.perform(delete("/" + companyId + "/companyManagers/" + companyManagerId)
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.data").value("User deleted"))
                    .andDo(print());
        }

        @Test
        void shouldNotDeleteNonExistingCompanyManager() throws Exception {
            UsersLogin userLogin = new UsersLogin("email", "asdf");
            String token = authService.authenticate(userLogin);

            UUID nonExistingManagerId = UUID.fromString("fed18c17-dc47-45f4-8b59-aafc04089a58");

            mockMvc.perform(delete("/" + companyId + "/companyManagers/" + nonExistingManagerId)
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.data").value(nullValue()))
                    .andExpect(jsonPath("$.errors[0].message").value("Company Manager with id: " + nonExistingManagerId + " is not found"))
                    .andDo(print());
        }
    }

}
