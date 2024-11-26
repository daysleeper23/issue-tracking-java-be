package org.projectmanagement.presentation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.projectmanagement.application.dto.company_managers.CreateCompanyManagers;
import org.projectmanagement.application.dto.company_managers.UpdateCompanyManagers;
import org.projectmanagement.application.dto.users.UsersLogin;

import org.projectmanagement.domain.services.AuthService;
import org.projectmanagement.test_data_factories.*;
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
    private AuthService authService;

    @Autowired
    private CompaniesDataFactory companiesDataFactory;

    @Autowired
    private UsersDataFactory usersDataFactory;

    @Autowired
    private RolesDataFactory rolesDataFactory;

    @Autowired
    private CompanyManagersDataFactory companyManagersDataFactory;

    UUID companyId;
    UUID superAdminRoleId;
    UUID user1Id;
    UUID user2Id;
    UUID companyManager1Id;
    UUID companyManager2Id;
    UUID companyManagerRoleId;
    String token;




    @BeforeEach
    void setUp() {
        companyId = companiesDataFactory.createCompany();

        superAdminRoleId = rolesDataFactory.createRoleWithAllPermissions("Super Admin", companyId, false);

        companyManagerRoleId = rolesDataFactory.createRoleWithAllPermissions("Company Manager", companyId, false);

        user1Id = usersDataFactory.createOwnerUser(companyId, "testuser@example.com", "hashedpassword");

        user2Id = usersDataFactory.createNonOwnerUser(companyId);


        companyManager1Id = companyManagersDataFactory.createCompanyManager(user1Id, superAdminRoleId, companyId);
        companyManager2Id = companyManagersDataFactory.createCompanyManager(user2Id, companyManagerRoleId, companyId);

        UsersLogin userLogin = new UsersLogin("testuser@example.com", "hashedpassword");
        token = authService.authenticate(userLogin);

    }

    @AfterEach
    void cleanUp() {
        companiesDataFactory.deleteAll();
        usersDataFactory.deleteAll();
        rolesDataFactory.deleteAll();
        companyManagersDataFactory.deleteAll();;
    }

    @Nested
    class GetCompanyManagers {
        @Test
        void getAllCorrectly() throws Exception {

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

            mockMvc.perform(get("/"+ companyId + "/companyManagers/" + companyManager1Id)
                    .header("Authorization", "Bearer " + token))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.data").isMap())
                    .andExpect(jsonPath("$.data.id").value(companyManager1Id.toString()))
                    .andDo(print());
        }
    }

    @Nested
    class PostRoles {
        @Test
        void shouldCreateCompanyManagerNormallyWithProperData() throws Exception{
            UUID user3Id = usersDataFactory.createNonOwnerUser(companyId);

            CreateCompanyManagers companyManagers = new CreateCompanyManagers(user3Id, companyId, superAdminRoleId);
            String roleJson = objectMapper.writeValueAsString(companyManagers);

            mockMvc.perform(post("/" + companyId + "/companyManagers")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(roleJson))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.data.roleId").value(superAdminRoleId.toString()))
                    .andDo(print());
        }

        @Test
        void shouldNotCreateCompanyManagerWithNonExistingUserId() throws Exception{
            UUID nonExistingUserId = UUID.fromString("fed18c17-dc47-45f4-8b59-aafc04089a58");

            CreateCompanyManagers companyManagers = new CreateCompanyManagers(nonExistingUserId, companyId, superAdminRoleId);
            String roleJson = objectMapper.writeValueAsString(companyManagers);

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
        void shouldNotCreateCompanyManagerWithNonExistingSuperAdminRoleIdWithId() throws Exception{
            UUID user3Id = usersDataFactory.createNonOwnerUser(companyId);

            UUID nonExistingSuperAdminRoleId = UUID.fromString("fed18c17-dc47-45f4-8b59-aafc04089a58");

            CreateCompanyManagers companyManagers = new CreateCompanyManagers(user3Id, companyId, nonExistingSuperAdminRoleId);

            String roleJson = objectMapper.writeValueAsString(companyManagers);

            mockMvc.perform(post("/" + companyId + "/companyManagers")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(roleJson))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.data").value(nullValue()))
                    .andExpect(jsonPath("$.errors[0].message").value("Role with id: " + nonExistingSuperAdminRoleId + " is not found"))
                    .andDo(print());
        }

        @Test
        void shouldNotCreateCompanyManagerWithNonManagerRole() throws Exception{
            UUID user3Id = usersDataFactory.createNonOwnerUser(companyId);

            UUID nonManagerId = rolesDataFactory.createRoleWithoutPermissions("some role", companyId);

            CreateCompanyManagers companyManagers = new CreateCompanyManagers(user3Id, companyId, nonManagerId);
            String roleJson = objectMapper.writeValueAsString(companyManagers);

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

            UpdateCompanyManagers updateDto = new UpdateCompanyManagers(companyManagerRoleId);
            String updateJson = objectMapper.writeValueAsString(updateDto);

            mockMvc.perform(patch("/" + companyId + "/companyManagers/" + companyManager1Id)
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(updateJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.data.roleId").value(companyManagerRoleId.toString()))
                    .andDo(print());
        }

        @Test
        void shouldNotUpdateCompanyManagerWithNonManagerRole() throws Exception {

            UUID roleWithoutPermissionsId = rolesDataFactory.createRoleWithoutPermissions("some role", companyId);

            UpdateCompanyManagers updateDto = new UpdateCompanyManagers(roleWithoutPermissionsId);
            String updateJson = objectMapper.writeValueAsString(updateDto);

            mockMvc.perform(patch("/" + companyId + "/companyManagers/" + companyManager1Id)
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

            UUID nonExistingManagerId = UUID.fromString("fed18c17-dc47-45f4-8b59-aafc04089a58");

            UpdateCompanyManagers updateDto = new UpdateCompanyManagers(companyManagerRoleId);
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

            mockMvc.perform(delete("/" + companyId + "/companyManagers/" + companyManager1Id)
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.data").value("User deleted"))
                    .andDo(print());
        }

        @Test
        void shouldNotDeleteNonExistingCompanyManager() throws Exception {

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
