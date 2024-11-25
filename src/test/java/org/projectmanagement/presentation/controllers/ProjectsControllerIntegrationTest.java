package org.projectmanagement.presentation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.projectmanagement.application.dto.projects.ProjectsCreate;
import org.projectmanagement.application.dto.projects.ProjectsUpdate;
import org.projectmanagement.application.dto.users.UsersLogin;
import org.projectmanagement.domain.enums.DefaultStatus;
import org.projectmanagement.domain.services.AuthService;
import org.projectmanagement.test_data_factories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
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
public class ProjectsControllerIntegrationTest {

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

    UUID companyId;
    UUID workspaceId;
    UUID userId;
    UUID projectId;
    UUID roleId;
    String token;


    @BeforeEach
    void setUp() {

        companyId = companiesDataFactory.createCompany();

        workspaceId = workspacesDataFactory.createWorkspace(companyId);

        userId = usersDataFactory.createOwnerUser(companyId, "testuser@example.com", "hashedpassword");

        projectId = projectsDataFactory.createProject("Project Name", workspaceId, userId);

        roleId = rolesDataFactory.createRoleWithAllPermissions("custom name", companyId, false);

        workspaceMemberRolesDataFactory.createWorkspacesMembersRole(userId, roleId, workspaceId);

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
    }

    @Nested
    class GetProjects {
        @Test
        void shouldReturnAllProjectsByWorkspaceId() throws Exception {

            mockMvc.perform(get("/" + companyId + "/" + workspaceId + "/projects")
                    .header("Authorization", "Bearer " + token))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data", hasSize(1)))
                    .andDo(print());
        }
    }

    @Nested
    class CreateProject {
        @Test
        void shouldCreateProjectSuccessfully() throws Exception {
            ProjectsCreate createDto = new ProjectsCreate(
                    "New Project",
                    "A new project description",
                    Instant.now().plusSeconds(7200),
                    Instant.now(),
                    2,
                    DefaultStatus.IN_PROGRESS,
                    userId,
                    workspaceId
            );
            String createJson = objectMapper.writeValueAsString(createDto);

            mockMvc.perform(post("/" + companyId + "/" + workspaceId + "/projects")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(createJson))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.data.name").value("New Project"))
                    .andDo(print());
        }
    }

    @Nested
    class UpdateProject {
        @Test
        void shouldUpdateProjectSuccessfully() throws Exception {
            ProjectsUpdate updateDto = new ProjectsUpdate(
                    "Updated Project",
                    "Updated project description",
                    Instant.now().plusSeconds(10800),
                    Instant.now(),
                    3,
                    DefaultStatus.DONE
            );
            String updateJson = objectMapper.writeValueAsString(updateDto);

            mockMvc.perform(patch("/" + companyId + "/" + workspaceId + "/projects/" + projectId)
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(updateJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.data.name").value("Updated Project"))
                    .andDo(print());
        }
    }

    @Nested
    class DeleteProject {
        @Test
        void shouldDeleteProjectSuccessfully() throws Exception {

            mockMvc.perform(delete("/" + companyId + "/" + workspaceId + "/projects/" + projectId)
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.data").value("Project Deleted"))
                    .andDo(print());
        }

        @Test
        void shouldNotDeleteNonExistingProject() throws Exception {
            UUID nonExistingProjectId = UUID.randomUUID();

            mockMvc.perform(delete("/" + companyId + "/" + workspaceId + "/projects/" + nonExistingProjectId)
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.data").value(nullValue()))
                    .andExpect(jsonPath("$.errors[0].message").value("Project with id: " + nonExistingProjectId + " was not found."))
                    .andDo(print());
        }
    }
}
