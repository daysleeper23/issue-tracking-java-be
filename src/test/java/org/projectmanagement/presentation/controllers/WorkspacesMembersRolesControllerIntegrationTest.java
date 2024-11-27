package org.projectmanagement.presentation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.projectmanagement.application.dto.users.UsersLogin;
import org.projectmanagement.application.dto.workspacesmembersroles.WorkspacesMembersRolesCreate;
import org.projectmanagement.application.exceptions.AppMessage;
import org.projectmanagement.presentation.config.DataInitializer;
import org.projectmanagement.presentation.config.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class WorkspacesMembersRolesControllerIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private DataInitializer dataInitializer;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${JWT_SECRET}")
    private String jwtSecret;

    private final UUID companyId = UUID.fromString("3bbb31a7-0915-4d77-b185-e939a5b9cd38");
    private final UUID ownerId = UUID.fromString("156de810-1339-469d-9611-8443cdda88d1");
    private final UUID companyManagerId = UUID.fromString("acd9c8ef-87f3-4eae-bd92-238b01b746b6");
    private final UUID workspaceManager1Id = UUID.fromString("f980e928-2ba4-47c5-abaa-cc675be24671");
    private final UUID workspaceManager2Id = UUID.fromString("7f28dd30-549a-41af-bd42-1239651fe4f5");
    private final UUID member1Id = UUID.fromString("4a7d763b-9ec7-46a0-9861-9c0dfb8e7129");
    private final UUID member2Id = UUID.fromString("745b3885-cb08-4076-b97e-626c56e10e0f");
    private final UUID member3Id = UUID.fromString("f3b3b3b3-3b3b-3b3b-3b3b-3b3b3b3b3b3b");
    private final UUID member4Id = UUID.fromString("4b4b4b4b-4b4b-4b4b-4b4b-4b4b4b4b4b4b");

    private final UUID adminRoleId = UUID.fromString("0c3bc98e-f22b-42ae-875e-0ab066ecd327");
    private final UUID companyManagerRoleId = UUID.fromString("1e8cdfaa-4bd4-4111-866f-6292f26d97f1");
    private final UUID workspaceManagerRoleId = UUID.fromString("22bd7e77-7830-46c7-943a-c706b174c390");
    private final UUID memberRoleId = UUID.fromString("238bb464-40fb-4bdb-8d10-d2e97c4849a7");

    private final UUID workspace1Id = UUID.fromString("caa47ad6-6a0c-4733-82cc-af51b5412d94");
    private final UUID workspace2Id = UUID.fromString("de7968b1-762a-4ea6-b6ec-cab609005012");

    private final UUID project1Id = UUID.fromString("d9b2efea-9447-49a2-b904-f3be8261f8d2");
    private final UUID project2Id = UUID.fromString("4863b61b-f2ac-488d-b80f-68748cd0978b");

    private final UUID wmr_wm_1Id = UUID.fromString("259c7860-9652-4e60-94db-ec497788d2b5");
    private final UUID wmr_m1_1Id = UUID.fromString("6993455f-8ef8-4f3e-bef8-9db3bcb99ba4");
    private final UUID wmr_m2_1Id = UUID.fromString("20af44ce-0a3b-40ce-842e-12b5a53bf059");
    private final UUID wmr_wm_2Id = UUID.fromString("698c4a40-de56-4951-9d97-f92082a6744e");
    private final UUID wmr_m1_2Id = UUID.fromString("65c30158-e11c-42d2-a9b9-0834cb37b673");
    private final UUID wmr_m2_2Id = UUID.fromString("5b066a05-fee4-447f-b85e-6d7625eee310");

    @Autowired
    private JwtHelper jwtHelper;

    private static String adminToken;
    private static String ownerEmail = "owner@fs19java.com";

    @BeforeEach
    public void setUp() {
        dataInitializer.initializeCompanies();
        dataInitializer.initializeUsers();
        dataInitializer.initializeRolesPermissions();
        dataInitializer.initializeWorkspacesAndMemberRoles();
        dataInitializer.initializeProjectsAndMembers();
    }

    @BeforeAll
    public static void setUp(@Autowired JwtHelper jwtHelper) {
        adminToken = jwtHelper.generateToken(new UsersLogin(ownerEmail, "12345678"));
    }

    @Nested
    class GetWorkspacesMembersRoles {
        @Test
        void getAllRolesOfWorkspaceMembersCorrectly() throws Exception {
            mockMvc.perform(get("/"+ companyId + "/" + workspace1Id + "/members/roles")
                    .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(3)))
                .andDo(print());
        }
    }

    @Nested
    //add member to a workspace with a role
    class CreateWorkspacesMembersRoles {
        @Test
        void shouldAddMemberToWorkspaceWithCorrectData() throws Exception {
            mockMvc.perform(post("/" + companyId + "/" + workspace1Id + "/members/roles")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\n" +
                            "    \"userId\": \"" + member3Id + "\",\n" +
                            "    \"roleId\": \"" + memberRoleId + "\"\n" +
                            "}")
                    .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.id").value(notNullValue()))
                .andExpect(jsonPath("$.data.userId").value(member3Id.toString()))
                .andExpect(jsonPath("$.data.workspaceId").value(workspace1Id.toString()))
                .andExpect(jsonPath("$.data.roleId").value(memberRoleId.toString()))
                .andDo(print());
        }

        @Test
        void shouldNotAddMemberIfAlreadyJoinedWorkspace() throws Exception {
            mockMvc.perform(post("/" + companyId + "/" + workspace1Id + "/members/roles")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\n" +
                        "    \"userId\": \"" + member1Id + "\",\n" +
                        "    \"roleId\": \"" + memberRoleId + "\"\n" +
                        "}")
                    .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.errors[0].message").value(AppMessage.WRM_USER_ALREADY_IN_WORKSPACE.getMessage()))
                .andDo(print());
        }

        @Test
        public void shouldNotAddNonExistingUserToWorkspace() throws Exception {
            mockMvc.perform(post("/" + companyId + "/" + workspace1Id + "/members/roles")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\n" +
                        "    \"userId\": \"" + UUID.randomUUID() + "\",\n" +
                        "    \"roleId\": \"" + memberRoleId + "\"\n" +
                        "}")
                    .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.errors[0].message").value(AppMessage.USER_NOT_FOUND.getMessage()))
                .andDo(print());
        }

        @Test
        public void shouldNotAddMemberToWorkspaceWithNonExistingRole() throws Exception {
            mockMvc.perform(post("/" + companyId + "/" + workspace1Id + "/members/roles")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\n" +
                        "    \"userId\": \"" + member3Id + "\",\n" +
                        "    \"roleId\": \"" + UUID.randomUUID() + "\"\n" +
                        "}")
                    .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.errors[0].message").value(AppMessage.ROLE_NOT_FOUND.getMessage()))
                .andDo(print());
        }

        @Test
        public void shouldNotAddMemberToNonExistingWorkspace() throws Exception {
            mockMvc.perform(post("/" + companyId + "/" + UUID.randomUUID() + "/members/roles")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\n" +
                        "    \"userId\": \"" + member3Id + "\",\n" +
                        "    \"roleId\": \"" + memberRoleId + "\"\n" +
                        "}")
                    .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.errors[0].message").value(AppMessage.WORKSPACE_NOT_FOUND.getMessage()))
                .andDo(print());
        }

        @Test
        public void shouldNotAddMemberToWorkspaceWithSuperAdminRole() throws Exception {
            mockMvc.perform(post("/" + companyId + "/" + workspace1Id + "/members/roles")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\n" +
                        "    \"userId\": \"" + member3Id + "\",\n" +
                        "    \"roleId\": \"" + adminRoleId + "\"\n" +
                        "}")
                    .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.errors[0].message").value(AppMessage.WMR_INVALID_ROLE.getMessage()))
                .andDo(print());
        }

        @Test
        public void shouldNotAddMemberToWorkspaceWithCompanyManagersRole() throws Exception {
            mockMvc.perform(post("/" + companyId + "/" + workspace1Id + "/members/roles")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\n" +
                        "    \"userId\": \"" + member3Id + "\",\n" +
                        "    \"roleId\": \"" + companyManagerRoleId + "\"\n" +
                        "}")
                    .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.errors[0].message").value(AppMessage.WMR_INVALID_ROLE.getMessage()))
                .andDo(print());
        }
    }

    @Nested
    class UpdateWorkspacesMembersRoles {
        @Test
        void shouldUpdateRoleForMemberInWorkspaceWithCorrectData() throws Exception {
            WorkspacesMembersRolesCreate wmrc = new WorkspacesMembersRolesCreate(
                    member1Id,
                    workspaceManagerRoleId
            );
            String wmrJson = objectMapper.writeValueAsString(wmrc);

            mockMvc.perform(put("/" + companyId + "/" + workspace1Id + "/members/roles" + "/" + wmr_m1_1Id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(wmrJson)
                            .header("Authorization", "Bearer " + adminToken))

                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.data.id").value(notNullValue()))
                    .andExpect(jsonPath("$.data.workspaceId").value(workspace1Id.toString()))
                    .andExpect(jsonPath("$.data.roleId").value(workspaceManagerRoleId.toString()))
                    .andExpect(jsonPath("$.data.userId").value(member1Id.toString()))
                    .andDo(print());
        }

        @Test
        void shouldNotUpdateRoleForMemberInWorkspaceWithNonExistingRole() throws Exception {
            WorkspacesMembersRolesCreate wmrc = new WorkspacesMembersRolesCreate(
                    member1Id,
                    UUID.randomUUID()
            );
            String wmrJson = objectMapper.writeValueAsString(wmrc);

            mockMvc.perform(put("/" + companyId + "/" + workspace1Id + "/members/roles" + "/" + wmr_m1_1Id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(wmrJson)
                    .header("Authorization", "Bearer " + adminToken))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.errors[0].message").value(AppMessage.ROLE_NOT_FOUND.getMessage()))
                    .andDo(print());
        }

        @Test
        void shouldNotUpdateRoleForMemberInWorkspaceWithNonExistingWorkspace() throws Exception {
            WorkspacesMembersRolesCreate wmrc = new WorkspacesMembersRolesCreate(
                    member1Id,
                    workspaceManagerRoleId
            );
            String wmrJson = objectMapper.writeValueAsString(wmrc);

            mockMvc.perform(put("/" + companyId + "/" + UUID.randomUUID() + "/members/roles" + "/" + wmr_m1_1Id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(wmrJson)
                    .header("Authorization", "Bearer " + adminToken))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.errors[0].message").value(AppMessage.WMR_INVALID_WORKSPACE.getMessage()))
                    .andDo(print());
        }

        @Test
        void shouldNotUpdateRoleForMemberInWorkspaceWithNonExistingUser() throws Exception {
            WorkspacesMembersRolesCreate wmrc = new WorkspacesMembersRolesCreate(
                    UUID.randomUUID(),
                    workspaceManagerRoleId
            );
            String wmrJson = objectMapper.writeValueAsString(wmrc);

            mockMvc.perform(put("/" + companyId + "/" + workspace1Id + "/members/roles" + "/" + wmr_m1_1Id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(wmrJson)
                    .header("Authorization", "Bearer " + adminToken))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.errors[0].message").value(AppMessage.WMR_INVALID_USER.getMessage()))
                    .andDo(print());
        }

        @Test
        void shouldNotUpdateRoleForMemberInWorkspaceWithSuperAdminRole() throws Exception {
            WorkspacesMembersRolesCreate wmrc = new WorkspacesMembersRolesCreate(
                    member1Id,
                    adminRoleId
            );
            String wmrJson = objectMapper.writeValueAsString(wmrc);

            mockMvc.perform(put("/" + companyId + "/" + workspace1Id + "/members/roles" + "/" + wmr_m1_1Id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(wmrJson)
                    .header("Authorization", "Bearer " + adminToken))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.errors[0].message").value(AppMessage.WMR_INVALID_ROLE.getMessage()))
                    .andDo(print());
        }

        @Test
        void shouldNotUpdateRoleForMemberInWorkspaceWithCompanyManagersRole() throws Exception {
            WorkspacesMembersRolesCreate wmrc = new WorkspacesMembersRolesCreate(
                    member1Id,
                    companyManagerRoleId
            );
            String wmrJson = objectMapper.writeValueAsString(wmrc);

            mockMvc.perform(put("/" + companyId + "/" + workspace1Id + "/members/roles" + "/" + wmr_m1_1Id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(wmrJson)
                    .header("Authorization", "Bearer " + adminToken))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.errors[0].message").value(AppMessage.WMR_INVALID_ROLE.getMessage()))
                    .andDo(print());
        }

        @Test
        void shouldNotUpdateRoleForMemberInWorkspaceWithDifferentWorkspace() throws Exception {
            WorkspacesMembersRolesCreate wmrc = new WorkspacesMembersRolesCreate(
                    member1Id,
                    workspaceManagerRoleId
            );
            String wmrJson = objectMapper.writeValueAsString(wmrc);

            mockMvc.perform(put("/" + companyId + "/" + workspace2Id + "/members/roles" + "/" + wmr_m1_1Id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(wmrJson)
                    .header("Authorization", "Bearer " + adminToken))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.errors[0].message").value(AppMessage.WMR_INVALID_WORKSPACE.getMessage()))
                    .andDo(print());
        }

        @Test
        void shouldNotUpdateRoleForMemberInWorkspaceWithDifferentUser() throws Exception {
            WorkspacesMembersRolesCreate wmrc = new WorkspacesMembersRolesCreate(
                    member2Id,
                    workspaceManagerRoleId
            );
            String wmrJson = objectMapper.writeValueAsString(wmrc);

            mockMvc.perform(put("/" + companyId + "/" + workspace1Id + "/members/roles" + "/" + wmr_m1_1Id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(wmrJson)
                    .header("Authorization", "Bearer " + adminToken))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.errors[0].message").value(AppMessage.WMR_INVALID_USER.getMessage()))
                    .andDo(print());
        }

        @Test
        void shouldNotUpdateRoleForMemberInWorkspaceWithInvalidId() throws Exception {
            WorkspacesMembersRolesCreate wmrc = new WorkspacesMembersRolesCreate(
                    member1Id,
                    workspaceManagerRoleId
            );
            String wmrJson = objectMapper.writeValueAsString(wmrc);

            mockMvc.perform(put("/" + companyId + "/" + workspace1Id + "/members/roles" + "/" + UUID.randomUUID())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(wmrJson)
                    .header("Authorization", "Bearer " + adminToken))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.errors[0].message").value(AppMessage.WMR_ROLE_NOT_FOUND.getMessage()))
                    .andDo(print());
        }
    }
}
