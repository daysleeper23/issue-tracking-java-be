package org.projectmanagement.presentation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.projectmanagement.application.dto.project_members.ProjectMemberCreate;
import org.projectmanagement.application.dto.project_members.ProjectMemberUpdate;
import org.projectmanagement.application.dto.users.UsersLogin;
import org.projectmanagement.domain.services.AuthService;
import org.projectmanagement.test_data_factories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class ProjectMembersControllerTest {
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
    private WorkspacesDataFactory workspacesDataFactory;

    @Autowired
    private ProjectsDataFactory projectsDataFactory;

    @Autowired
    private RolesDataFactory rolesDataFactory;

    @Autowired
    private WorkspaceMemberRolesDataFactory workspaceMemberRolesDataFactory;

    @Autowired
    private ProjectMembersDataFactory projectMembersDataFactory;

    UUID companyId;
    UUID workspaceId;
    UUID userId;
    UUID user2Id;
    UUID projectId;
    UUID project2Id;
    UUID roleId;
    UUID projectMemberId;
    UUID project2MemberId;
    UUID workspaceMemberRoleId;
    String token;

    @BeforeEach
    void setUp() {

        companyId = companiesDataFactory.createCompany();

        workspaceId = workspacesDataFactory.createWorkspace(companyId);

        userId = usersDataFactory.createOwnerUser(companyId, "testuser@example.com", "hashedpassword");

        user2Id = usersDataFactory.createOwnerUser(companyId, "testuser2@example.com", "hashedpassword");

        projectId = projectsDataFactory.createProject("Project Name", workspaceId, userId);
        project2Id = projectsDataFactory.createProject("Project Name 2", workspaceId, userId);

        roleId = rolesDataFactory.createRoleWithAllPermissions("custom name", companyId, false);

        workspaceMemberRoleId = workspaceMemberRolesDataFactory.createWorkspacesMembersRole(userId, roleId, workspaceId);

        projectMemberId = projectMembersDataFactory.addMemberToProject(projectId, userId);
        project2MemberId = projectMembersDataFactory.addMemberToProject(project2Id, userId);

        UsersLogin userLogin = new UsersLogin("testuser@example.com", "hashedpassword");
        token = authService.authenticate(userLogin);

    }

    @AfterEach
    void cleanUp() {
        companiesDataFactory.deleteAll();
        usersDataFactory.deleteAll();
        workspacesDataFactory.deleteAll();
        workspaceMemberRolesDataFactory.deleteAll();
        projectsDataFactory.deleteAll();
        rolesDataFactory.deleteAll();
        projectMembersDataFactory.deleteAll();
    }

    @Nested
    class GetProjectMembers {
        @Test
        void shouldReturnAllProjectMembersByProjectId() throws Exception {
            mockMvc.perform(get("/" + projectId + "/projectMembers")
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data", hasSize(1)))
                    .andDo(print());
        }

        @Test
        void shouldReturnProjectMemberById() throws Exception {
            mockMvc.perform(get("/" + projectId + "/projectMembers/" + projectMemberId)
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.data").isMap())
                    .andDo(print());
        }

        @Test
        void shouldReturnNotFoundWhenProjectMemberByIdDoesNotExist() throws Exception {
            UUID nonExistingProjectMemberId = UUID.randomUUID();

            mockMvc.perform(get("/" + projectId + "/projectMembers/" + nonExistingProjectMemberId)
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.errors[0].message").value("Project Member with id: " + nonExistingProjectMemberId + " was not found."))
                    .andDo(print());
        }

        @Test
        void shouldReturnNotFoundWhenGettingProjectMembersForNonExistingProject() throws Exception {
            UUID nonExistingProjectId = UUID.randomUUID();

            mockMvc.perform(get("/" + nonExistingProjectId + "/projectMembers")
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.errors[0].message").value("Project with id: " + nonExistingProjectId + " was not found."))
                    .andDo(print());
        }

    }

    @Nested
    class CreateProjectMember {
        @Test
        void shouldCreateProjectMemberSuccessfully() throws Exception {
            ProjectMemberCreate createDto = new ProjectMemberCreate(user2Id, false);
            String createJson = objectMapper.writeValueAsString(createDto);

            mockMvc.perform(post("/" + projectId + "/projectMembers")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(createJson))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.data.userId").value(user2Id.toString()))
                    .andDo(print());
        }

        @Test
        void shouldReturnNotFoundWhenCreatingProjectMemberForNonExistingProject() throws Exception {
            UUID nonExistingProjectId = UUID.randomUUID();
            ProjectMemberCreate createDto = new ProjectMemberCreate(userId, false);
            String createJson = objectMapper.writeValueAsString(createDto);

            mockMvc.perform(post("/" + nonExistingProjectId + "/projectMembers")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(createJson))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.errors[0].message").value("Project with id: " + nonExistingProjectId + " was not found."))
                    .andDo(print());
        }

        @Test
        void shouldReturnConflictWhenAddingDuplicateMember() throws Exception {
            ProjectMemberCreate createDto = new ProjectMemberCreate(userId, false);
            String createJson = objectMapper.writeValueAsString(createDto);

            // Add the same member twice
            mockMvc.perform(post("/" + projectId + "/projectMembers")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(createJson))
                    .andExpect(status().isConflict());
        }

        @Test
        void shouldNotCreateProjectMemberIfUserIsNotMemberOfWorkspace() throws Exception {
            UUID userId = usersDataFactory.createNonOwnerUser(companyId);

            ProjectMemberCreate createDto = new ProjectMemberCreate(userId, false);
            String createJson = objectMapper.writeValueAsString(createDto);

            mockMvc.perform(post("/" + projectId + "/projectMembers")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(createJson))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.errors[0].message").value("Given user is not a member of the workspace and can not be added to given the project."))
                    .andDo(print());
        }

    }

    @Nested
    class UpdateProject {
        @Test
        void shouldUpdateProjectMemberSuccessfully() throws Exception {
            ProjectMemberUpdate updateDto = new ProjectMemberUpdate(true);
            String updateJson = objectMapper.writeValueAsString(updateDto);

            mockMvc.perform(patch("/" + projectId + "/projectMembers/" + projectMemberId)
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(updateJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.data.subscribed").value(true))
                    .andDo(print());
        }
    }

    @Nested
    class DeleteProject {
        @Test
        void shouldDeleteProjectSuccessfully() throws Exception {
            mockMvc.perform(delete("/" + projectId + "/projectMembers/" + projectMemberId)
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.data").value("Project Member Deleted"))
                    .andDo(print());
        }
    }
}
