package org.projectmanagement.presentation.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.projectmanagement.application.dto.tasks.TasksCreate;
import org.projectmanagement.application.dto.users.UsersLogin;
import org.projectmanagement.domain.entities.Roles;
import org.projectmanagement.domain.enums.DefaultStatus;
import org.projectmanagement.domain.services.AuthService;
import org.projectmanagement.test_data_factories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class TasksControllerIntegrationTest {


    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthService authService;

    @Autowired
    private UsersDataFactory usersDataFactory;

    @Autowired
    private CompaniesDataFactory companiesDataFactory;

    @Autowired
    private RolesDataFactory rolesDataFactory;

    @Autowired
    private CompanyManagersDataFactory companyManagersDataFactory;

    @Autowired
    private WorkspacesDataFactory workspacesDataFactory;

    @Autowired
    private InvitationsDataFactory invitationsDataFactory;

    @Autowired
    private ProjectsDataFactory projectsDataFactory;

    private UUID userId;
    private UUID userId2;
    private String jwtToken;
    private String jwtToken2;
    private UUID companyId;
    private UUID adminRoleId;
    private UUID workspaceId;
    private UUID projectId;

    @BeforeEach
    public void before() {

        companyId = companiesDataFactory.createCompany();
        userId = usersDataFactory.createOwnerUser(companyId, "testuser2@example.com", "hashedpassword");
        userId2 = usersDataFactory.createNonOwnerUser(companyId, "testuser3@example.com", "hashedpassword");
        UsersLogin userLogin = new UsersLogin("testuser2@example.com", "hashedpassword");
        UsersLogin userLogin2 = new UsersLogin("testuser3@example.com", "hashedpassword");
        jwtToken = authService.authenticate(userLogin);
        jwtToken2 = authService.authenticate(userLogin2);
        workspaceId = workspacesDataFactory.createWorkspace(companyId);
        adminRoleId = rolesDataFactory.createRoleWithAllPermissions(Roles.SystemRoles.ADMIN.getName(), companyId, false);
        companyManagersDataFactory.createCompanyManager(userId, adminRoleId, companyId);
        projectId = projectsDataFactory.createProject("Project Name", workspaceId, userId);

    }

    @AfterEach
    public void after() {
        usersDataFactory.deleteAll();
        companiesDataFactory.deleteAll();
        companiesDataFactory.deleteAll();
        companyManagersDataFactory.deleteAll();
        invitationsDataFactory.deleteAll();
    }


    @Nested
    public class PostTask {
        @Test
        void testAddTask() throws Exception {
            TasksCreate tasksCreate = new TasksCreate("Task Name",
                    "Task Description",
                    projectId.toString(),
                    (short) 0,
                    DefaultStatus.TODO.toString(),
                    userId.toString(),
                    Instant.now(),
                    Instant.now().plus(5, ChronoUnit.DAYS));
            mockMvc.perform(post("/tasks")
                    .header("Authorization", "Bearer " + jwtToken)
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(tasksCreate)))
                    .andExpect(status().isCreated());
        }
    }
}
