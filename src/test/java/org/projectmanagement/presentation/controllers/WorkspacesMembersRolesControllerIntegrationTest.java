package org.projectmanagement.presentation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.projectmanagement.application.dto.workspacesmembersroles.WorkspacesMembersRolesCreate;
import org.projectmanagement.infrastructure.InMemoryDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    private ObjectMapper objectMapper;

    InMemoryDatabase inMemoryDatabase;
    UUID companyId = UUID.fromString("b541ade4-9cfa-4664-b9e3-d9923ae02fb4");
    UUID workspaceId1 = UUID.fromString("6892ddd0-8a88-4aac-a562-1e1656732f9f");
    UUID workspaceId2 = UUID.fromString("f7e6c463-7930-446c-b871-59db53cf5c01");
    UUID roleAdminId = UUID.fromString("7b149139-6b39-4e5c-9e24-70c092df4a5d");
    UUID roleDeveloperId = UUID.fromString("78b4fe40-5f93-40ef-9095-7b25c7bb62ff");
    UUID roleProjectDirectorId = UUID.fromString("fed18c17-dc47-45f4-8b59-aafc04089a58");

    @Nested
    class GetWorkspacesMembersRoles {
        @Test
        void getAllRolesOfWorkspaceMembersCorrectly() throws Exception {


            mockMvc.perform(get("/"+ companyId + "/" + workspaceId1 + "/members/roles"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data", hasSize(2)))
                    .andDo(print());
        }
    }

    @Nested
    class CreateWorkspacesMembersRoles {
        @Test
        void createRoleForWorkspaceMemberCorrectly() throws Exception {
            UUID newUserId = UUID.randomUUID();
            WorkspacesMembersRolesCreate wmrc = new WorkspacesMembersRolesCreate(
                    newUserId,
                    workspaceId1,
                    roleAdminId
            );
            String wmrJson = objectMapper.writeValueAsString(wmrc);

            mockMvc.perform(post("/" + companyId + "/" + workspaceId1 + "/members/roles")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(wmrJson))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.data.id").value(notNullValue()))
                    .andExpect(jsonPath("$.data.workspaceId").value(workspaceId1.toString()))
                    .andExpect(jsonPath("$.data.roleId").value(roleAdminId.toString()))
                    .andExpect(jsonPath("$.data.userId").value(newUserId.toString()))
                    .andDo(print());
        }
    }

    @Nested
    class UpdateWorkspacesMembersRoles {
        @Test
        void updateRoleForWorkspaceMemberCorrectly() throws Exception {
            UUID newUserId = UUID.randomUUID();
            WorkspacesMembersRolesCreate wmrc = new WorkspacesMembersRolesCreate(
                    newUserId,
                    workspaceId1,
                    roleAdminId
            );
            String wmrJson = objectMapper.writeValueAsString(wmrc);

            mockMvc.perform(post("/" + companyId + "/" + workspaceId1 + "/members/roles")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(wmrJson))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.data.id").value(notNullValue()))
                    .andExpect(jsonPath("$.data.workspaceId").value(workspaceId1.toString()))
                    .andExpect(jsonPath("$.data.roleId").value(roleAdminId.toString()))
                    .andExpect(jsonPath("$.data.userId").value(newUserId.toString()))
                    .andDo(print());

            WorkspacesMembersRolesCreate newWmrc = new WorkspacesMembersRolesCreate(
                    newUserId,
                    workspaceId1,
                    roleDeveloperId
            );
            String newWmrJson = objectMapper.writeValueAsString(newWmrc);

            mockMvc.perform(post("/" + companyId + "/" + workspaceId1 + "/members/roles")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(newWmrJson))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.data.id").value(notNullValue()))
                    .andExpect(jsonPath("$.data.workspaceId").value(workspaceId1.toString()))
                    .andExpect(jsonPath("$.data.roleId").value(roleDeveloperId.toString()))
                    .andExpect(jsonPath("$.data.userId").value(newUserId.toString()))
                    .andDo(print());
        }
    }
}
