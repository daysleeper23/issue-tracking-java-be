package org.projectmanagement.presentation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.projectmanagement.application.dto.roles_permissions.RolesPermissionsCreate;
import org.projectmanagement.application.dto.roles_permissions.RolesPermissionsUpdate;
import org.projectmanagement.application.dto.users.UsersLogin;
import org.projectmanagement.domain.entities.*;
import org.projectmanagement.domain.repository.*;
import org.projectmanagement.domain.services.AuthService;
import org.projectmanagement.test_data_factories.CompaniesDataFactory;
import org.projectmanagement.test_data_factories.RolesDataFactory;
import org.projectmanagement.test_data_factories.RolesPermissionsDataFactory;
import org.projectmanagement.test_data_factories.UsersDataFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class RolesPermissionsControllerIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PermissionsRepoJpa permissionsRepoJpa;

    @Autowired
    private AuthService authService;

    @Autowired
    private CompaniesDataFactory companiesDataFactory;

    @Autowired
    private UsersDataFactory usersDataFactory;

    @Autowired
    private RolesDataFactory rolesDataFactory;

    @Autowired
    private RolesPermissionsDataFactory rolesPermissionsDataFactory;

    UUID companyId;
    UUID superAdminRoleId;
    UUID user1Id;
    UUID user2Id;
    String token;
    Permissions p1;
    Permissions p2;

    @BeforeEach
    void setUp() {

        companyId = companiesDataFactory.createCompany();

        superAdminRoleId = rolesDataFactory.createRoleWithAllPermissions("Super Admin", companyId, true);

        user1Id = usersDataFactory.createOwnerUser(companyId, "testuser@example.com", "hashedpassword");

        user2Id = usersDataFactory.createOwnerUser(companyId, "testuser2@example.com", "hashedpassword");

        p1 = permissionsRepoJpa.findByName("COMPANY_READ").orElse(null);
        p2 = permissionsRepoJpa.findByName("WORKSPACE_CREATE").orElse(null);

        UsersLogin userLogin = new UsersLogin("testuser@example.com", "hashedpassword");
        token = authService.authenticate(userLogin);

    }

    @AfterEach
    void cleanUp() {
        companiesDataFactory.deleteAll();
        usersDataFactory.deleteAll();
        rolesDataFactory.deleteAll();
    }

    @Nested
    class GetRolesPermissions {
        @Test
        void getAllByCompanyIdCorrectly() throws Exception {

            mockMvc.perform(get("/"+ companyId + "/rolesPermissions")
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data", hasSize(21))) //default size of permissions is 21
                    .andDo(print());
        }
    }


    @Nested
    class PostRolesPermissions {
        @Test
        void shouldCreateRolesPermissionsNormallyWithProperData() throws Exception{

            Permissions p1 = permissionsRepoJpa.findByName("COMPANY_READ").orElse(null);
            Permissions p2 = permissionsRepoJpa.findByName("WORKSPACE_CREATE").orElse(null);

            List<UUID> permissionIds = new ArrayList<>(List.of(p1.getId(), p2.getId()));

            RolesPermissionsCreate rpDto = new RolesPermissionsCreate("Test Role", permissionIds);
            String roleJson = objectMapper.writeValueAsString(rpDto);

            mockMvc.perform(post("/" + companyId + "/rolesPermissions")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(roleJson))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data", hasSize(2)))
                    .andDo(print());
        }

        @Test
        void shouldNotCreateRolesPermissionsWithDuplicateRoleName() throws Exception{

            Permissions p1 = permissionsRepoJpa.findByName("COMPANY_READ").orElse(null);
            Permissions p2 = permissionsRepoJpa.findByName("WORKSPACE_CREATE").orElse(null);

            List<UUID> permissionIds = new ArrayList<>(List.of(p1.getId(), p2.getId()));


            RolesPermissionsCreate rpDto = new RolesPermissionsCreate("Super Admin", permissionIds);
            String roleJson = objectMapper.writeValueAsString(rpDto);

            mockMvc.perform(post("/" + companyId + "/rolesPermissions")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(roleJson))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.data").value(nullValue()))
                    .andExpect(jsonPath("$.errors[0].message").value("Role with name: " + rpDto.roleName() + " already exists."))
                    .andDo(print());;
        }

        @Test
        void shouldNotCreateRolesPermissionsWithNonExistingPermissionId() throws Exception{
            UUID noExistingPermissionsId = UUID.fromString("fed18c17-dc47-45f4-8b59-aafc04089a58");
            List<UUID> permissionIds = new ArrayList<>(List.of(noExistingPermissionsId));

            RolesPermissionsCreate rpDto = new RolesPermissionsCreate("Test Role", permissionIds );
            String roleJson = objectMapper.writeValueAsString(rpDto);

            mockMvc.perform(post("/" + companyId + "/rolesPermissions")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(roleJson))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.data").value(nullValue()))
                    .andExpect(jsonPath("$.errors[0].message").value("One of the given Permissions was not found."))
                    .andDo(print());;
        }
    }

    @Nested
    class PatchRolesPermissions {
        @Test
        void shouldUpdateRolesPermissionsNormallyWithProperData() throws Exception{

            UUID nonSystemRoleWithoutPermissionsId = rolesDataFactory.createRoleWithoutPermissions("Custom Role", companyId);

            Permissions p1 = permissionsRepoJpa.findByName("COMPANY_READ").orElse(null);
            Permissions p2 = permissionsRepoJpa.findByName("WORKSPACE_CREATE").orElse(null);

            List<UUID> permissionIds = new ArrayList<>(List.of(p1.getId(), p2.getId()));


            RolesPermissionsCreate rpDto = new RolesPermissionsCreate("Custom Role", permissionIds);
            String roleJson = objectMapper.writeValueAsString(rpDto);

            mockMvc.perform(patch("/" + companyId + "/rolesPermissions/" + nonSystemRoleWithoutPermissionsId)
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(roleJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data", hasSize(2)))
                    .andDo(print());
        }


        @Test
        void shouldNotUpdateRolesPermissionsForSystemRole() throws Exception{

            List<UUID> permissionIds = new ArrayList<>(List.of(p1.getId(), p2.getId()));

            RolesPermissionsUpdate rpDto = new RolesPermissionsUpdate(permissionIds);
            String roleJson = objectMapper.writeValueAsString(rpDto);

            mockMvc.perform(patch("/" + companyId + "/rolesPermissions/" + superAdminRoleId)
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(roleJson))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.code").value(403))
                    .andExpect(jsonPath("$.errors[0].message").value("System Roles can not be updated. The role: Super Admin is a system role."))
                    .andDo(print());
        }
    }

    @Nested
    class RemoveRolesPermissions {
        @Test
        void shouldRemoveRolesPermissionsWithValidData() throws Exception {

            UUID nonSystemRoleWithoutPermissionsId = rolesDataFactory.createRoleWithoutPermissions("Custom Role", companyId);

            UUID rolePermissionId = rolesPermissionsDataFactory.addPermissionToRole(nonSystemRoleWithoutPermissionsId, p1.getId());

            List<UUID> permissionIds = new ArrayList<>(List.of(p1.getId()));

            RolesPermissionsUpdate rpDto = new RolesPermissionsUpdate(permissionIds);
            String roleJson = objectMapper.writeValueAsString(rpDto);

            mockMvc.perform(delete("/" + companyId + "/rolesPermissions/" + nonSystemRoleWithoutPermissionsId)
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(roleJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.data").value("Permissions Removed from role"))
                    .andDo(print());
        }

        @Test
        void shouldNotRemoveRolesPermissionsForSystemRole() throws Exception {

            List<UUID> permissionIds = new ArrayList<>(List.of(p1.getId()));

            RolesPermissionsUpdate rpDto = new RolesPermissionsUpdate(permissionIds);
            String roleJson = objectMapper.writeValueAsString(rpDto);

            mockMvc.perform(delete("/" + companyId + "/rolesPermissions/" + superAdminRoleId)
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(roleJson))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.code").value(403))
                    .andExpect(jsonPath("$.errors[0].message").value("System Roles can not be updated. The role: Super Admin is a system role."))
                    .andDo(print());
        }


        @Test
        void shouldNotRemoveRolesPermissionsWithNonExistingRoleId() throws Exception {
            UUID nonExistingRoleId = UUID.fromString("fed18c17-dc47-45f4-8b59-aafc04089a58");

            List<UUID> permissionIds = new ArrayList<>(List.of(p1.getId()));

            RolesPermissionsUpdate rpDto = new RolesPermissionsUpdate(permissionIds);
            String roleJson = objectMapper.writeValueAsString(rpDto);

            mockMvc.perform(delete("/" + companyId + "/rolesPermissions/" + nonExistingRoleId)
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(roleJson))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.data").value(nullValue()))
                    .andExpect(jsonPath("$.errors[0].message").value("Role with id:" + nonExistingRoleId + " was not found."))
                    .andDo(print());
        }
    }


}
