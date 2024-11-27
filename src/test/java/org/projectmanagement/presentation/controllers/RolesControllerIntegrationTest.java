package org.projectmanagement.presentation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.projectmanagement.application.dto.roles.RolesCreate;
import org.projectmanagement.application.dto.users.UsersLogin;
import org.projectmanagement.domain.services.AuthService;
import org.projectmanagement.test_data_factories.CompaniesDataFactory;
import org.projectmanagement.test_data_factories.CompanyManagersDataFactory;
import org.projectmanagement.test_data_factories.RolesDataFactory;
import org.projectmanagement.test_data_factories.UsersDataFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class RolesControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RolesDataFactory rolesDataFactory;

    @Autowired
    private CompaniesDataFactory companiesDataFactory;

    @Autowired
    private UsersDataFactory usersDataFactory;

    @Autowired
    private CompanyManagersDataFactory companyManagersDataFactory;

    @Autowired
    private AuthService authService;

    private UUID companyId;
    private UUID userId;
    private UUID adminRoleId;
    private UUID developerRoleId;
    private UUID projectDirectorRoleId;
    private UUID companyManagerId;
    private String token;

    @BeforeEach
    void setUp() {
        // Set up data for the company and roles
        companyId = companiesDataFactory.createCompany();

        userId = usersDataFactory.createOwnerUser(companyId, "testuser@example.com", "hashedpassword");

        adminRoleId = rolesDataFactory.createRoleWithAllPermissions("Admin", companyId ,true);

        developerRoleId = rolesDataFactory.createRoleWithoutPermissions("Developer", companyId);
        projectDirectorRoleId = rolesDataFactory.createRoleWithoutPermissions("Team Leader", companyId);

        companyManagerId = companyManagersDataFactory.createCompanyManager(userId, adminRoleId, companyId);

        UsersLogin userLogin = new UsersLogin("testuser@example.com", "hashedpassword");
        token = authService.authenticate(userLogin);
    }

    @AfterEach
    void cleanUp() {
        rolesDataFactory.deleteAll();
        companiesDataFactory.deleteAll();
        usersDataFactory.deleteAll();
    }

    @Nested
    class GetRoles {
        @Test
        void getAllRolesCorrectly() throws Exception {
            mockMvc.perform(get("/" + companyId + "/roles")
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data", hasSize(3)))
                    .andDo(print());
        }
    }

    @Nested
    class PostRoles {
        @Test
        void shouldCreateRoleNormallyWithProperData() throws Exception {
            RolesCreate newRole = new RolesCreate("Project Manager");
            String roleJson = objectMapper.writeValueAsString(newRole);

            mockMvc.perform(post("/" + companyId + "/roles")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(roleJson))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.data.name").value("Project Manager"))
                    .andDo(print());
        }

        @Test
        void shouldNotCreateRoleWithBlankName() throws Exception {
            RolesCreate newRole = new RolesCreate("");
            String roleJson = objectMapper.writeValueAsString(newRole);

            mockMvc.perform(post("/" + companyId + "/roles")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(roleJson))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.data").value(nullValue()))
                    .andExpect(jsonPath("$.errors[0].message").value("name cannot be blank"));
        }

        @Test
        void shouldNotCreateRoleWithDuplicateName() throws Exception {
            RolesCreate duplicateRole = new RolesCreate("Admin"); // "Admin" already exists
            String roleJson = objectMapper.writeValueAsString(duplicateRole);

            mockMvc.perform(post("/" + companyId + "/roles")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(roleJson))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andDo(print());
        }

        @Test
        void shouldNotCreateRoleWithMissingName() throws Exception {
            String roleJson = "{ }"; // No name field

            mockMvc.perform(post("/" + companyId + "/roles")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(roleJson))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.errors[0].message").value("name cannot be blank"))
                    .andDo(print());
        }
    }

    @Nested
    class UpdateRoles {
        @Test
        void shouldNotUpdateSystemRole() throws Exception {

            RolesCreate updatedRole = new RolesCreate("Updated System Role Name");
            String updatedRoleJson = objectMapper.writeValueAsString(updatedRole);

            mockMvc.perform(put("/" + companyId + "/roles/" + adminRoleId)
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(updatedRoleJson))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.errors[0].message").value("System roles cannot be updated."))
                    .andDo(print());
        }
    }

    @Nested
    class DeleteRoles {
        @Test
        void shouldDeleteNonSystemRoleSuccessfully() throws Exception {
            mockMvc.perform(delete("/" + companyId + "/roles/" + developerRoleId)
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isNoContent())
                    .andDo(print());
        }

        @Test
        void shouldNotDeleteSystemRole() throws Exception {
            mockMvc.perform(delete("/" + companyId + "/roles/" + adminRoleId) // adminRoleId is system role
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.errors[0].message").value("System roles cannot be deleted."))
                    .andDo(print());
        }

        @Test
        void shouldReturnNotFoundForNonExistingRole() throws Exception {
            UUID nonExistingRoleId = UUID.randomUUID();

            mockMvc.perform(delete("/" + companyId + "/roles/" + nonExistingRoleId)
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.errors[0].message").value("Role with id: " + nonExistingRoleId + " was not found."))
                    .andDo(print());
        }
    }



}
