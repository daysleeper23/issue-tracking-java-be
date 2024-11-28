package org.projectmanagement.presentation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.projectmanagement.application.dto.tasks.TasksCreate;
import org.projectmanagement.application.dto.tasks.TasksUpdate;
import org.projectmanagement.application.dto.users.UsersLogin;
import org.projectmanagement.application.exceptions.AppMessage;
import org.projectmanagement.domain.entities.Roles;
import org.projectmanagement.domain.enums.DefaultStatus;
import org.projectmanagement.domain.services.AuthService;
import org.projectmanagement.presentation.response.GlobalResponse;
import org.projectmanagement.test_data_factories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.hamcrest.core.IsNull.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    @Autowired
    private ProjectMembersDataFactory projectMembersDataFactory;
    @Autowired
    private TasksDataFactory tasksDataFactory;
    @Autowired
    private TaskSubscribersDataFactory taskSubscribersDataFactory;

    private UUID userId;
    private UUID userId2;
    private UUID userId3;
    private String jwtToken;
    private String jwtToken2;
    private String jwtToken3;
    private UUID companyId;
    private UUID adminRoleId;
    private UUID workspaceId;
    private UUID projectId;
    private UUID taskId;

    @BeforeEach
    public void before() {

        companyId = companiesDataFactory.createCompany();
        userId = usersDataFactory.createOwnerUser(companyId, "testuser1@example.com", "hashedpassword");
        userId2 = usersDataFactory.createNonOwnerUser(companyId, "testuser2@example.com", "hashedpassword");
        userId3 = usersDataFactory.createNonOwnerUser(companyId, "testuser3@example.com", "hashedpassword");
        UsersLogin userLogin = new UsersLogin("testuser1@example.com", "hashedpassword");
        UsersLogin userLogin2 = new UsersLogin("testuser2@example.com", "hashedpassword");
        UsersLogin userLogin3 = new UsersLogin("testuser3@example.com", "hashedpassword");
        jwtToken = authService.authenticate(userLogin);
        jwtToken2 = authService.authenticate(userLogin2);
        jwtToken3 = authService.authenticate(userLogin3);
        workspaceId = workspacesDataFactory.createWorkspace(companyId);
        adminRoleId = rolesDataFactory.createRoleWithAllPermissions(Roles.SystemRoles.ADMIN.getName(), companyId, false);
        companyManagersDataFactory.createCompanyManager(userId, adminRoleId, companyId);
        companyManagersDataFactory.createCompanyManager(userId2, adminRoleId, companyId);
        projectId = projectsDataFactory.createProject("Project Name", workspaceId, userId);
        taskId = tasksDataFactory.createTask("Task Name",projectId,userId2);
        tasksDataFactory.createTask("Task Name 2",projectId,userId2);
        projectMembersDataFactory.addMemberToProject(projectId, userId2);
        projectMembersDataFactory.addMemberToProject(projectId, userId);
        taskSubscribersDataFactory.addSubscriberToTask(taskId, userId);
    }

    @AfterEach
    public void after() {
        usersDataFactory.deleteAll();
        companiesDataFactory.deleteAll();
        companiesDataFactory.deleteAll();
        companyManagersDataFactory.deleteAll();
        invitationsDataFactory.deleteAll();
        projectMembersDataFactory.deleteAll();
        projectsDataFactory.deleteAll();
        tasksDataFactory.deleteAll();
        taskSubscribersDataFactory.deleteAll();
    }


    @Nested
    public class PostTask {
        @Test
        void testAddTask() throws Exception {
            TasksCreate tasksCreate = new TasksCreate("Task Name 1",
                    "Task Description",
                    projectId.toString(),
                    (short) 0,
                    DefaultStatus.TODO.toString(),
                    userId.toString(),
                    Instant.now(),
                    Instant.now().plus(5, ChronoUnit.DAYS));
            mockMvc.perform(post("/" + companyId +"/"+projectId +"/" + "/tasks")
                            .header("Authorization", "Bearer " + jwtToken)
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(tasksCreate)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.status").value(GlobalResponse.SUCCESS))
                    .andExpect(jsonPath("$.data.name").value(tasksCreate.name()))
                    .andExpect(jsonPath("$.data.description").value(tasksCreate.description()))
            ;
        }

        @Test
        void shouldNotAddTaskWithExistedTasksName() throws Exception {
            TasksCreate tasksCreate = new TasksCreate("Task Name",
                    "Task Description",
                    projectId.toString(),
                    (short) 0,
                    "TODO",
                    userId.toString(),
                    Instant.now().plus(1, ChronoUnit.DAYS),
                    Instant.now().plus(5, ChronoUnit.DAYS)
            );

            mockMvc.perform(post("/" + companyId +"/"+projectId +"/" + "/tasks")
                            .header("Authorization", "Bearer " + jwtToken)
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(tasksCreate)))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.status").value(GlobalResponse.ERROR))
                    .andExpect(jsonPath("$.data").value(nullValue()))
                    .andExpect(jsonPath("$.errors[0].message").value("Duplicate resource: Task name already exist"))
            ;
        }

        @Test
        void shouldNotAddTaskIfPriorityValueIsInvalid() throws Exception {
            TasksCreate tasksCreate = new TasksCreate("Task Name",
                    "Task Description",
                    projectId.toString(),
                    (short) 5,
                    "TODO",
                    userId.toString(),
                    Instant.now(),
                    Instant.now().plus(5, ChronoUnit.DAYS));

            mockMvc.perform(post("/" + companyId +"/"+projectId +"/" + "/tasks")
                            .header("Authorization", "Bearer " + jwtToken2)
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(tasksCreate)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(GlobalResponse.ERROR))
                    .andExpect(jsonPath("$.data").value(nullValue()))
                    .andExpect(jsonPath("$.errors[0].message").value("priority must be lesser than 4"))
            ;
        }

        @Test
        void shouldNotAddTaskIfStatusValueIsInvalid() throws Exception {
            TasksCreate tasksCreate = new TasksCreate("Task Name",
                    "Task Description",
                    projectId.toString(),
                    (short) 0,
                    "not a status",
                    userId.toString(),
                    Instant.now(),
                    Instant.now().plus(5, ChronoUnit.DAYS));

            mockMvc.perform(post("/" + companyId +"/"+projectId +"/" + "/tasks")
                            .header("Authorization", "Bearer " + jwtToken2)
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(tasksCreate)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(GlobalResponse.ERROR))
                    .andExpect(jsonPath("$.data").value(nullValue()))
                    .andExpect(jsonPath("$.errors[0].message").value("status invalid value"))
            ;
        }

        @Test
        void shouldNotAddTaskIfStartAtIsAfterEndedAt() throws Exception {
            TasksCreate tasksCreate = new TasksCreate("Task Name",
                    "Task Description",
                    projectId.toString(),
                    (short) 0,
                    "TODO",
                    userId.toString(),
                    Instant.now().plus(7, ChronoUnit.DAYS),
                    Instant.now().plus(5, ChronoUnit.DAYS)
            );

            mockMvc.perform(post("/" + companyId +"/"+projectId +"/" + "/tasks")
                            .header("Authorization", "Bearer " + jwtToken)
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(tasksCreate)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(GlobalResponse.ERROR))
                    .andExpect(jsonPath("$.data").value(nullValue()))
                    .andExpect(jsonPath("$.errors[0].message").value("Ended at cannot be before started at"))
            ;
        }

        @Test
        void shouldNotAddTaskIfNotAMemberOfProject() throws Exception {
            TasksCreate tasksCreate = new TasksCreate("Task Name 1",
                    "Task Description",
                    projectId.toString(),
                    (short) 0,
                    DefaultStatus.TODO.toString(),
                    userId.toString(),
                    Instant.now(),
                    Instant.now().plus(5, ChronoUnit.DAYS));
            mockMvc.perform(post("/" + companyId +"/"+projectId +"/" + "/tasks")
                            .header("Authorization", "Bearer " + jwtToken3)
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(tasksCreate)))
                    .andExpect(status().isForbidden())
                    .andDo(print())
            ;
        }
    }

    @Nested
    class GetTasks {
        @Test
        void getTasks() throws Exception {

            mockMvc.perform(get("/" + companyId +"/"+projectId +"/" + "/tasks")
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(GlobalResponse.SUCCESS))
                    .andExpect(jsonPath("$.data.length()").value(2))
            ;
        }

        @Test
        void getAllTasksAssociated() throws Exception {
            mockMvc.perform(get("/" + companyId  + "/tasks")
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(GlobalResponse.SUCCESS))
                    .andExpect(jsonPath("$.data.length()").value(1))
                    .andDo(print())
            ;
        }
        @Test
        void getTasksByProjectId() throws Exception {
            mockMvc.perform(get("/" + companyId +"/"+projectId +"/" + "/tasks")
                            .header("Authorization", "Bearer " + jwtToken)
                            .param("projectId", projectId.toString()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(GlobalResponse.SUCCESS))
                    .andExpect(jsonPath("$.data.length()").value(2))
                    .andDo(print())
            ;
        }

    }

    @Nested
    class PutTasks{
        @Test
        void updateTask() throws Exception {

            TasksUpdate tasksUpdate = new TasksUpdate("Task Name 1",
                    "Task Description Updated",
                    Short.parseShort("1"),
                    DefaultStatus.IN_PROGRESS.toString(),
                    userId.toString(),
                    null,
                    null);
            mockMvc.perform(put("/" + companyId +"/"+ projectId +"/" + "/tasks/" + taskId)
                            .header("Authorization", "Bearer " + jwtToken)
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(tasksUpdate)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(GlobalResponse.SUCCESS))
                    .andExpect(jsonPath("$.data.name").value(tasksUpdate.getName()))
                    .andExpect(jsonPath("$.data.description").value(tasksUpdate.getDescription()))
                    .andExpect(jsonPath("$.data.status").value(DefaultStatus.IN_PROGRESS.toString()))
                    .andDo(print());
        }

        @Test
        void shouldNotUpdateTask() throws Exception {

            TasksUpdate tasksUpdate = new TasksUpdate("Task Name 1",
                    "Task Description Updated",
                    Short.parseShort("1"),
                    DefaultStatus.IN_PROGRESS.toString(),
                    userId.toString(),
                    null,
                    null);
            mockMvc.perform(put("/" + companyId +"/"+projectId +"/" + "/tasks/" + taskId)
                            .header("Authorization", "Bearer " + jwtToken3)
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(tasksUpdate)))
                    .andExpect(status().isForbidden())
                    .andDo(print());
            ;
        }

        @Test
        void subscribeToTasks() throws Exception {
            mockMvc.perform(put("/" + companyId +"/"+projectId +"/" + "/tasks/" + taskId + "/subscribe")
                            .header("Authorization", "Bearer " + jwtToken2))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(GlobalResponse.SUCCESS))
                    .andDo(print());
        }

        @Test
        void shouldNotSubscribeToTask() throws Exception {
            mockMvc.perform(put("/" + companyId +"/"+projectId +"/" + "/tasks/" + taskId + "/subscribe")
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.status").value(GlobalResponse.ERROR))
                    .andExpect(jsonPath("$.errors[0].message").value("Duplicate resource: User already subscribed to this task"))
                    .andExpect(jsonPath("$.data").value(nullValue()))
                    .andDo(print());
        }

        @Test
        void shouldNotSubscribeToTaskIfNotInProject() throws Exception {
            mockMvc.perform(put("/" + companyId +"/"+projectId +"/" + "/tasks/" + taskId + "/subscribe")
                            .header("Authorization", "Bearer " + jwtToken3))
                    .andExpect(status().isForbidden())
                    .andDo(print());
        }

        @Test
        void unsubscribeToTasks() throws Exception {
            mockMvc.perform(put("/" + companyId +"/"+projectId +"/" + "/tasks/" + taskId + "/unsubscribe")
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(GlobalResponse.SUCCESS))
                    .andDo(print());
        }

        @Test
        void shouldNotUnsubscribeToTasksIfNotInProject() throws Exception {
            mockMvc.perform(put("/" + companyId +"/"+projectId +"/" + "/tasks/" + taskId + "/unsubscribe")
                            .header("Authorization", "Bearer " + jwtToken3))
                    .andExpect(status().isForbidden())
                    .andDo(print());
        }

        @Test
        void shouldNotUnsubscribeToTasksAgain() throws Exception {
            mockMvc.perform(put("/" + companyId +"/"+projectId +"/" + "/tasks/" + taskId + "/unsubscribe")
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(GlobalResponse.SUCCESS))
                    .andExpect(jsonPath("$.data").value(true))
                    .andDo(print());

            mockMvc.perform(put("/" + companyId +"/"+projectId +"/" + "/tasks/" + taskId + "/unsubscribe")
                            .header("Authorization", "Bearer " + jwtToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(GlobalResponse.SUCCESS))
                    .andExpect(jsonPath("$.data").value(false))
                    .andDo(print());
        }
    }
}
