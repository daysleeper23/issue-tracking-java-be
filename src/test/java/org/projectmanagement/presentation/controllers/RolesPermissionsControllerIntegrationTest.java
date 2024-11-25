package org.projectmanagement.presentation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.projectmanagement.application.dto.roles_permissions.RolesPermissionsCreate;
import org.projectmanagement.application.dto.roles_permissions.RolesPermissionsUpdate;
import org.projectmanagement.application.dto.users.UsersLogin;
import org.projectmanagement.domain.entities.*;
import org.projectmanagement.domain.repository.*;
import org.projectmanagement.domain.repository.jpa.CompaniesJpaRepository;
import org.projectmanagement.domain.services.AuthService;
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
    private RolesPermissionsRepoJpa rolesPermissionsRepoJpa;

    @Autowired
    private CompaniesJpaRepository companiesJpaRepository;

    @Autowired
    private PermissionsRepoJpa permissionsRepoJpa;

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
    UUID rolePermissionsId;
    Permissions companyReadPermission;


    @BeforeEach
    void setUp() {
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

        companyReadPermission = permissionsRepoJpa.findByName("COMPANY_READ").orElse(null);

        RolesPermissions rp = rolesPermissionsRepoJpa.save(RolesPermissions.builder()
                        .permissionId(companyReadPermission.getId())
                        .roleId(roleAdminId)
                        .build());

        rolePermissionsId = rp.getRoleId();

    }

    @Nested
    class GetRolesPermissions {
        @Test
        void getAllByCompanyIdCorrectly() throws Exception {
            UsersLogin userLogin = new UsersLogin("email", "asdf");

            String token = authService.authenticate(userLogin);

            mockMvc.perform(get("/"+ companyId + "/rolesPermissions")
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data", hasSize(1)))
                    .andDo(print());
        }
    }


    @Nested
    class PostRolesPermissions {
        @Test
        void shouldCreateRolesPermissionsNormallyWithProperData() throws Exception{
            rolesPermissionsRepoJpa.deleteAll();
            UsersLogin userLogin = new UsersLogin("email", "asdf");

            String token = authService.authenticate(userLogin);
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
            UsersLogin userLogin = new UsersLogin("email", "asdf");

            String token = authService.authenticate(userLogin);
            Permissions p1 = permissionsRepoJpa.findByName("COMPANY_READ").orElse(null);
            Permissions p2 = permissionsRepoJpa.findByName("WORKSPACE_CREATE").orElse(null);

            List<UUID> permissionIds = new ArrayList<>(List.of(p1.getId(), p2.getId()));


            RolesPermissionsCreate rpDto = new RolesPermissionsCreate("ADMIN", permissionIds);
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

            UsersLogin userLogin = new UsersLogin("email", "asdf");

            String token = authService.authenticate(userLogin);

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
            rolesPermissionsRepoJpa.deleteAll();
            UsersLogin userLogin = new UsersLogin("email", "asdf");

            String token = authService.authenticate(userLogin);

            Roles nonSystemRole = rolesRepoJpa.save(Roles.builder()
                    .name("CUSTOM ROLE")
                    .companyId(companyId)
                    .isDeleted(false)
                    .isSystemRole(false)
                    .build());

            Permissions p1 = permissionsRepoJpa.findByName("COMPANY_READ").orElse(null);
            Permissions p2 = permissionsRepoJpa.findByName("WORKSPACE_CREATE").orElse(null);

            List<UUID> permissionIds = new ArrayList<>(List.of(p1.getId(), p2.getId()));


            RolesPermissionsCreate rpDto = new RolesPermissionsCreate("Test Role", permissionIds);
            String roleJson = objectMapper.writeValueAsString(rpDto);

            mockMvc.perform(patch("/" + companyId + "/rolesPermissions/" + nonSystemRole.getId())
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
            UsersLogin userLogin = new UsersLogin("email", "asdf");

            String token = authService.authenticate(userLogin);

            Permissions p1 = permissionsRepoJpa.findByName("COMPANY_READ").orElse(null);
            Permissions p2 = permissionsRepoJpa.findByName("WORKSPACE_CREATE").orElse(null);

            List<UUID> permissionIds = new ArrayList<>(List.of(p1.getId(), p2.getId()));

            RolesPermissionsCreate rpDto = new RolesPermissionsCreate("Test Role", permissionIds);
            String roleJson = objectMapper.writeValueAsString(rpDto);

            mockMvc.perform(patch("/" + companyId + "/rolesPermissions/" + rolePermissionsId)
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(roleJson))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.code").value(403))
                    .andExpect(jsonPath("$.errors[0].message").value("System Roles can not be updated. The role: ADMIN is a system role."))
                    .andDo(print());
        }
    }

    @Nested
    class RemoveRolesPermissions {
        @Test
        void shouldRemoveRolesPermissionsWithValidData() throws Exception {
            UsersLogin userLogin = new UsersLogin("email", "asdf");
            String token = authService.authenticate(userLogin);
            Roles nonSystemRole = rolesRepoJpa.save(Roles.builder()
                    .name("CUSTOM ROLE")
                    .companyId(companyId)
                    .isDeleted(false)
                    .isSystemRole(false)
                    .build());

            Permissions p1 = permissionsRepoJpa.findByName("COMPANY_READ").orElse(null);

            RolesPermissions rpFromDb = rolesPermissionsRepoJpa.save(RolesPermissions.builder()
                            .roleId(nonSystemRole.getId())
                            .permissionId(p1.getId())
                    .build());


            List<UUID> permissionIds = new ArrayList<>(List.of(p1.getId()));


            RolesPermissionsCreate rpDto = new RolesPermissionsCreate("Test Role", permissionIds);
            String roleJson = objectMapper.writeValueAsString(rpDto);

            mockMvc.perform(delete("/" + companyId + "/rolesPermissions/" + rpFromDb.getRoleId())
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
            UsersLogin userLogin = new UsersLogin("email", "asdf");
            String token = authService.authenticate(userLogin);

            Permissions p1 = permissionsRepoJpa.findByName("COMPANY_READ").orElse(null);
            List<UUID> permissionIds = new ArrayList<>(List.of(p1.getId()));

            RolesPermissionsUpdate rpDto = new RolesPermissionsUpdate(permissionIds);
            String roleJson = objectMapper.writeValueAsString(rpDto);

            mockMvc.perform(delete("/" + companyId + "/rolesPermissions/" + rolePermissionsId)
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(roleJson))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.code").value(403))
                    .andExpect(jsonPath("$.errors[0].message").value("System Roles can not be updated. The role: ADMIN is a system role."))
                    .andDo(print());
        }


        @Test
        void shouldNotRemoveRolesPermissionsWithNonExistingRoleId() throws Exception {
            UUID nonExistingRoleId = UUID.fromString("fed18c17-dc47-45f4-8b59-aafc04089a58");
            Permissions p1 = permissionsRepoJpa.findByName("COMPANY_READ").orElse(null);
            List<UUID> permissionIds = new ArrayList<>(List.of(p1.getId()));

            RolesPermissionsUpdate rpDto = new RolesPermissionsUpdate(permissionIds);
            String roleJson = objectMapper.writeValueAsString(rpDto);

            UsersLogin userLogin = new UsersLogin("email", "asdf");
            String token = authService.authenticate(userLogin);

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
