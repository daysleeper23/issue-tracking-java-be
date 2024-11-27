package org.projectmanagement.presentation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.projectmanagement.application.dto.users.UsersCreate;
import org.projectmanagement.application.dto.users.UsersLogin;
import org.projectmanagement.application.dto.users.UsersUpdate;
import org.projectmanagement.domain.services.AuthService;
import org.projectmanagement.test_data_factories.CompaniesDataFactory;
import org.projectmanagement.test_data_factories.RolesDataFactory;
import org.projectmanagement.test_data_factories.UsersDataFactory;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class UsersControllerIntegrationTest {

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

    @BeforeEach
    void setUp() {
        companyId = companiesDataFactory.createCompany();
        adminRoleId = rolesDataFactory.createRoleWithAllPermissions("Admin", companyId, true);
        developerRoleId = rolesDataFactory.createRoleWithoutPermissions("Developer", companyId);
        projectDirectorRoleId = rolesDataFactory.createRoleWithoutPermissions("Team Leader", companyId);
        userId = usersDataFactory.createOwnerUser(companyId, "testuser@example.com", "hashedpassword");


        UsersLogin userLogin = new UsersLogin("testuser@example.com", "hashedpassword");
        token = authService.authenticate(userLogin);
    }

    @AfterEach
    void tearDown() {
        usersDataFactory.deleteAll();
        rolesDataFactory.deleteAll();
        companiesDataFactory.deleteAll();
    }

    @Nested
    class CreateUser {
        @Test
        void shouldCreateUserWithProperData() throws Exception {
            UsersCreate user = new UsersCreate(
                    "John Doe",
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
