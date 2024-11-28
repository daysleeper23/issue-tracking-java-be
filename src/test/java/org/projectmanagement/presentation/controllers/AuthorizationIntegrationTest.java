package org.projectmanagement.presentation.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.projectmanagement.application.dto.users.UsersLogin;
import org.projectmanagement.application.dto.workspaces.WorkspacesCreate;
import org.projectmanagement.application.dto.workspaces.WorkspacesUpdate;
import org.projectmanagement.presentation.config.DataInitializer;
import org.projectmanagement.presentation.config.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthorizationIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private DataInitializer dataInitializer;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${JWT_SECRET}")
    private String jwtSecret;

    private UserDetailsService userDetailsService;

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

    private static String ownerEmail = "owner@fs19java.com";

    private UUID newWorkspace1Id;

    @Autowired
    private JwtHelper jwtHelper;

    private static String adminToken;

    @BeforeEach
    public void setUp() {
        dataInitializer.initializeCompanies();
        dataInitializer.initializeUsers();
        dataInitializer.initializeRolesPermissions();
        dataInitializer.initializeWorkspacesAndMemberRoles();
        dataInitializer.initializeProjectsAndMembers();
    }

    @Nested
    class AdminAuthorization {
        // Admin should be able to access all resources
        @BeforeAll
        public static void setUp(@Autowired JwtHelper jwtHelper) {
            adminToken = jwtHelper.generateToken(new UsersLogin(ownerEmail, "12345678"));
        }

        @Test
        public void shouldBeAbleToGetAllUsers() throws Exception {
            mockMvc.perform(get("/" + companyId + "/members")
                    .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andDo(print());
        }

        @Nested
        class Workspaces {
            @Test
            public void shouldBeAbleToGetAllWorkspaces() throws Exception {
                mockMvc.perform(get("/" + companyId + "/workspaces")
                    .header("Authorization", "Bearer " + adminToken))
                    .andExpect(status().isOk())
                    .andDo(print());
            }

            @Test
            public void shouldBeAbleToCreateWorkspace() throws Exception {
                WorkspacesCreate workspace = new WorkspacesCreate("Workspace Test Authorization", "Description 1");
                String wsJson = objectMapper.writeValueAsString(workspace);

                mockMvc.perform(post("/" + companyId + "/workspaces")
                    .header("Authorization", "Bearer " + adminToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(wsJson))
                    .andExpect(status().isCreated())
                    .andDo(print());

                mockMvc.perform(get("/" + companyId + "/workspaces")
                        .header("Authorization", "Bearer " + adminToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data", hasSize(3)))
                    .andDo(print());
            }

            @Test
            public void shouldBeAbleToUpdateWorkspaceById() throws Exception {
                WorkspacesUpdate workspace = WorkspacesUpdate.withName("Workspace Test Authorization");
                String wsJson = objectMapper.writeValueAsString(workspace);

                mockMvc.perform(patch("/" + companyId + "/workspaces/" + workspace1Id)
                    .header("Authorization", "Bearer " + adminToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(wsJson))
                    .andExpect(status().isOk())
                    .andDo(print());

                WorkspacesUpdate workspace2 = WorkspacesUpdate.withName("Workspace Test Authorization 2");
                String wsJson2 = objectMapper.writeValueAsString(workspace2);
                mockMvc.perform(patch("/" + companyId + "/workspaces/" + workspace2Id)
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(wsJson2))
                    .andExpect(status().isOk())
                    .andDo(print());
            }

            @Test
            public void shouldBeAbleToDeleteWorkspaceById() throws Exception {
                mockMvc.perform(delete("/" + companyId + "/workspaces/" + workspace2Id)
                    .header("Authorization", "Bearer " + adminToken))
                    .andExpect(status().isNoContent())
                    .andDo(print());

                mockMvc.perform(get("/" + companyId + "/workspaces")
                    .header("Authorization", "Bearer " + adminToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data", hasSize(1)))
                    .andDo(print());

                mockMvc.perform(delete("/" + companyId + "/workspaces/" + workspace1Id)
                        .header("Authorization", "Bearer " + adminToken))
                    .andExpect(status().isNoContent())
                    .andDo(print());

                mockMvc.perform(get("/" + companyId + "/workspaces")
                        .header("Authorization", "Bearer " + adminToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data", hasSize(0)))
                    .andDo(print());
            }

            @Test
            public void shouldBeAbleToAddMemberToWorkspace() {

            }
        }

        @Nested
        class Projects {
            @Test
            public void shouldBeAbleToGetAllProjects() throws Exception {
                mockMvc.perform(get("/" + companyId + "/" + workspace1Id + "/projects")
                    .header("Authorization", "Bearer " + adminToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data", hasSize(1)))
                    .andDo(print());

                mockMvc.perform(get("/" + companyId + "/" + workspace2Id + "/projects")
                        .header("Authorization", "Bearer " + adminToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data", hasSize(1)))
                    .andDo(print());
            }

            @Test
            public void shouldBeAbleToCreateProject() throws Exception {

                //TODO: Should status & priority have default values if the client do not send?
                mockMvc.perform(post("/" + companyId + "/" + workspace1Id + "/projects")
                    .header("Authorization", "Bearer " + adminToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("" +
                        "{\"name\": \"Project Test Authorization\"" +
                        ", \"description\": \"Description 1\" " +
                        ", \"leaderId\": \"" + member1Id + "\"" +
                        ", \"workspaceId\": \"" + workspace1Id + "\" }"))
                    .andExpect(status().isCreated())
                    .andDo(print());

                mockMvc.perform(get("/" + companyId + "/" + workspace1Id + "/projects")
                    .header("Authorization", "Bearer " + adminToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data", hasSize(2)))
                    .andDo(print());

                mockMvc.perform(post("/" + companyId + "/" + workspace2Id + "/projects")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("" +
                            "{\"name\": \"Project Test Authorization 2\"" +
                            ", \"description\": \"Description 2\" " +
                            ", \"leaderId\": \"" + member2Id + "\"" +
                            ", \"workspaceId\": \"" + workspace2Id + "\" }"))
                    .andExpect(status().isCreated())
                    .andDo(print());

                mockMvc.perform(get("/" + companyId + "/" + workspace2Id + "/projects")
                        .header("Authorization", "Bearer " + adminToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data", hasSize(2)))
                    .andDo(print());
            }

            @Test
            public void shouldBeAbleToUpdateProjectById() throws Exception {
                mockMvc.perform(patch("/" + companyId + "/" + workspace1Id + "/projects/" + project1Id)
                    .header("Authorization", "Bearer " + adminToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(Map.of(
                        "name", "Project Test Authorization Updated",
                        "description", "Description 1 Updated",
                        "leaderId", member1Id,
                        "workspaceId", workspace1Id
                    ))))
                    .andExpect(status().isOk())
                    .andDo(print());

                mockMvc.perform(patch("/" + companyId + "/" + workspace2Id + "/projects/" + project2Id)
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                            "name", "Project Test Authorization 2 Updated",
                            "description", "Description 2 Updated"
                        ))))
                    .andExpect(status().isOk())
                    .andDo(print());
            }

            @Test
            public void shouldBeAbleToDeleteProjectById() throws Exception {
                //TODO: Should we return NoContent or Ok?
                mockMvc.perform(delete("/" + companyId + "/" + workspace1Id + "/projects/" + project1Id)
                    .header("Authorization", "Bearer " + adminToken))
                    .andExpect(status().isOk())
                    .andDo(print());

                mockMvc.perform(get("/" + companyId + "/" + workspace1Id + "/projects")
                    .header("Authorization", "Bearer " + adminToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data", hasSize(0)))
                    .andDo(print());

                mockMvc.perform(delete("/" + companyId + "/" + workspace2Id + "/projects/" + project2Id)
                        .header("Authorization", "Bearer " + adminToken))
                    .andExpect(status().isOk())
                    .andDo(print());

                mockMvc.perform(get("/" + companyId + "/" + workspace2Id + "/projects")
                        .header("Authorization", "Bearer " + adminToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data", hasSize(0)))
                    .andDo(print());
            }
        }
    }
}
