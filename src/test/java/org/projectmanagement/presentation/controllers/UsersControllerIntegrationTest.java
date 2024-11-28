package org.projectmanagement.presentation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.projectmanagement.application.dto.users.UsersCreate;
import org.projectmanagement.application.dto.users.UsersLogin;
import org.projectmanagement.application.dto.users.UsersUpdate;
import org.projectmanagement.application.exceptions.AppMessage;
import org.projectmanagement.domain.services.AuthService;
import org.projectmanagement.presentation.config.DataInitializer;
import org.projectmanagement.presentation.config.JwtHelper;
import org.projectmanagement.test_data_factories.CompaniesDataFactory;
import org.projectmanagement.test_data_factories.RolesDataFactory;
import org.projectmanagement.test_data_factories.UsersDataFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class UsersControllerIntegrationTest {

    @Value("${JWT_SECRET}")
    private String jwtSecret;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CompaniesDataFactory companiesDataFactory;

    @Autowired
    private RolesDataFactory rolesDataFactory;

    @Autowired
    private UsersDataFactory usersDataFactory;

    @Autowired
    private AuthService authService;

    private UUID companyId;
    private UUID userId;
    private UUID adminRoleId;
    private UUID developerRoleId;
    private UUID projectDirectorRoleId;
    private String token;

    private static String ownerEmail = "owner@fs19java.com";
    private final UUID workspace1Id = UUID.fromString("caa47ad6-6a0c-4733-82cc-af51b5412d94");
    private final UUID workspace2Id = UUID.fromString("de7968b1-762a-4ea6-b6ec-cab609005012");
    private final UUID company2Id = UUID.fromString("3bbb31a7-0915-4d77-b185-e939a5b9cd38");
    private final UUID adminRoleId2 = UUID.fromString("0c3bc98e-f22b-42ae-875e-0ab066ecd327");
    private final UUID memberRoleId = UUID.fromString("238bb464-40fb-4bdb-8d10-d2e97c4849a7");
    private final UUID companyManagerRoleId = UUID.fromString("1e8cdfaa-4bd4-4111-866f-6292f26d97f1");

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private DataInitializer dataInitializer;

    private static String adminToken;

    @BeforeAll
    public static void setUp(@Autowired JwtHelper jwtHelper) {
        adminToken = jwtHelper.generateToken(new UsersLogin(ownerEmail, "12345678"));
    }

    @BeforeEach
    void setUp() {
        companyId = companiesDataFactory.createCompany();
        adminRoleId = rolesDataFactory.createRoleWithAllPermissions("Admin", companyId, true);
        developerRoleId = rolesDataFactory.createRoleWithoutPermissions("Developer", companyId);
        projectDirectorRoleId = rolesDataFactory.createRoleWithoutPermissions("Team Leader", companyId);
        userId = usersDataFactory.createOwnerUser(companyId, "testuser@example.com", "hashedpassword");


        UsersLogin userLogin = new UsersLogin("testuser@example.com", "hashedpassword");
        token = authService.authenticate(userLogin);

        dataInitializer.initializeCompanies();
        dataInitializer.initializeUsers();
        dataInitializer.initializeRolesPermissions();
        dataInitializer.initializeWorkspacesAndMemberRoles();
        dataInitializer.initializeProjectsAndMembers();
    }

    @AfterEach
    void tearDown() {
        usersDataFactory.deleteAll();
        rolesDataFactory.deleteAll();
        companiesDataFactory.deleteAll();
    }

    @Nested
    class CreateAdminOrCompanyManagers {
        @Test
        void shouldCreateCompanyManagersWithProperData() throws Exception {
            mockMvc.perform(post("/" + company2Id + "/members/admin")
                    .header("Authorization", "Bearer " + adminToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(Map.of(
                        "name", "Another CM",
                        "email", "another-cm@fs19java.com",
                        "password", "passwordHash",
                        "isActive", "true",
                        "roleId", companyManagerRoleId
                    ))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.name").value("Another CM"))
                .andDo(print());
        }

        @Test
        void shouldCreateAdminsWithProperData() throws Exception {
            mockMvc.perform(post("/" + company2Id + "/members/admin")
                    .header("Authorization", "Bearer " + adminToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(Map.of(
                        "name", "Another Admin",
                        "email", "another-admin@fs19java.com",
                        "password", "passwordHash",
                        "isActive", "true",
                        "roleId", adminRoleId2
                    ))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.name").value("Another Admin"))
                .andDo(print());
        }

        @Test
        void shouldNotCreateAdminOrCMIfRoleIsNotAdminOrCM() throws Exception {
            mockMvc.perform(post("/" + company2Id + "/members/admin")
                    .header("Authorization", "Bearer " + adminToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(Map.of(
                        "name", "Another A or CM",
                        "email", "another-user@fs19java.com",
                        "password", "passwordHash",
                        "isActive", "true",
                        "roleId", memberRoleId
                    ))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.errors[0].message").value(AppMessage.USER_INVALID_ROLE_FOR_ADMIN.getMessage()))
                .andDo(print());
        }
    }

    @Nested
    class CreateUser {
        @Test
        void shouldCreateMemberWithProperData() throws Exception {
            UsersCreate user = new UsersCreate(
                    "John Doe",
                    "jdoe@test.com",
                    "passwordHash",
                    null,
                    true,
                    workspace1Id,
                    memberRoleId
            );
            String userJson = objectMapper.writeValueAsString(user);

            mockMvc.perform(post("/" + company2Id + "/members")
                            .header("Authorization", "Bearer " + adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(userJson))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.data.name").value("John Doe"))
                    .andDo(print());
        }

        @Test
        void shouldNotCreateUserWithBlankName() throws Exception {
            UsersCreate user = new UsersCreate(
                    "",
                    "jdoe@test.com",
                    "passwordHash",
                    null,
                    true,
                    companyId,
                    adminRoleId

            );
            String userJson = objectMapper.writeValueAsString(user);

            mockMvc.perform(post("/" + companyId + "/members")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(userJson))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errors[0].message").value("name cannot be blank"))
                    .andDo(print());
        }

        @Test
        void shouldNotCreateUserWithBlankEmail() throws Exception {
            UsersCreate user = new UsersCreate(
                    "John Doe",
                    "",
                    "passwordHash",
                    null,
                    true,
                    companyId,
                    adminRoleId
            );
            String userJson = objectMapper.writeValueAsString(user);

            mockMvc.perform(post("/" + companyId + "/members")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(userJson))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errors[0].message").value("email cannot be blank"))
                    .andDo(print());
        }

        @Test
        void shouldNotCreateUserWithBlankPassword() throws Exception {
            UsersCreate user = new UsersCreate(
                "John Doe",
                "jdoe@test.com",
                "",
                null,
                true,
                companyId,
                adminRoleId
            );
            String userJson = objectMapper.writeValueAsString(user);
            mockMvc.perform(post("/" + companyId + "/members")
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(userJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].message").value("password cannot be blank"))
                .andDo(print());
        }

        @Test
        void shouldNotCreateUserIfEmailIsUsed() throws Exception {
            UsersCreate user = new UsersCreate(
                "John Doe",
                "owner@fs19java.com",
                "passwordHash",
                null,
                true,
                companyId,
                adminRoleId
            );
            String userJson = objectMapper.writeValueAsString(user);

            mockMvc.perform(post("/" + companyId + "/members")
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(userJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].message").value(AppMessage.EMAIL_ALREADY_IN_USE.getMessage()))
                .andDo(print());
        }

        @Test
        void shouldNotCreateUserIfWorkspaceExistsButInvalidRole() throws Exception {
            mockMvc.perform(post("/" + company2Id + "/members")
                    .header("Authorization", "Bearer " + adminToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(Map.of(
                        "name", "John Doe",
                        "email", "jdoe2@fs19java.com",
                        "password", "passwordHash",
                        "isActive", "true",
                        "workspaceId", workspace1Id,
                        "roleId", adminRoleId2
                    ))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].message").value(AppMessage.WMR_INVALID_ROLE.getMessage()))
                .andDo(print());

            mockMvc.perform(post("/" + company2Id + "/members")
                    .header("Authorization", "Bearer " + adminToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(Map.of(
                        "name", "John Doe",
                        "email", "jdoe2@fs19java.com",
                        "password", "passwordHash",
                        "isActive", "true",
                        "workspaceId", workspace1Id,
                        "roleId", companyManagerRoleId
                    ))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].message").value(AppMessage.WMR_INVALID_ROLE.getMessage()))
                .andDo(print());
        }
    }

    @Nested
    class UpdateUser {
        @Test
        void shouldUpdateNameCorrectly() throws Exception {
            UsersUpdate updatedUser = new UsersUpdate("Updated Name", null, null, null, null, null, null);
            String userJson = objectMapper.writeValueAsString(updatedUser);

            mockMvc.perform(patch("/" + companyId + "/members/" + userId)
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(userJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.name").value("Updated Name"))
                    .andDo(print());
        }

    }

    @Nested
    class DeleteUser {
        @Test
        void shouldDeleteUserCorrectly() throws Exception {
            UUID secondUserId = usersDataFactory.createNonOwnerUser(companyId, "User 2", "user2@test.com");

            mockMvc.perform(delete("/" + companyId + "/members/" + secondUserId)
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isOk())
                    .andDo(print());
        }

        @Test
        void shouldReturnNotFoundWhenDeletingNonExistentUser() throws Exception {
            UUID nonExistentUserId = UUID.randomUUID();

            mockMvc.perform(delete("/" + companyId + "/members/" + nonExistentUserId)
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.data").isEmpty())
                    .andDo(print());
        }

    }

    @Nested
    class GetUsers {
        @Test
        void shouldGetAllUsersCorrectly() throws Exception {
            mockMvc.perform(get("/" + companyId + "/members")
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data", hasSize(1)))
                    .andDo(print());
        }

        @Test
        void shouldReturnNotFoundForNonExistentUser() throws Exception {
            UUID nonExistentUserId = UUID.randomUUID();

            mockMvc.perform(get("/" + companyId + "/members/" + nonExistentUserId)
                            .header("Authorization", "Bearer " + token))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.data").isEmpty())
                    .andDo(print());
        }
    }
}
