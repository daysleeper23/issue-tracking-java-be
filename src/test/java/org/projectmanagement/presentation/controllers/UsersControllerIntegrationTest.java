package org.projectmanagement.presentation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.projectmanagement.application.dto.users.UsersCreate;
import org.projectmanagement.application.dto.users.UsersUpdate;
import org.projectmanagement.domain.entities.Roles;
import org.projectmanagement.infrastructure.InMemoryDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class UsersControllerIntegrationTest {

//    @Autowired
//    MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    InMemoryDatabase inMemoryDatabase;
//    UUID companyId = UUID.fromString("b541ade4-9cfa-4664-b9e3-d9923ae02fb4");
//    UUID roleAdminId = UUID.fromString("7b149139-6b39-4e5c-9e24-70c092df4a5d");
//    UUID roleDeveloperId = UUID.fromString("78b4fe40-5f93-40ef-9095-7b25c7bb62ff");
//    UUID roleProjectDirectorId = UUID.fromString("fed18c17-dc47-45f4-8b59-aafc04089a58");
//    UUID user1 = UUID.fromString("03093311-73ce-4264-99c9-886caa7bd1e1");
//    UUID user2 = UUID.fromString("6ea39d08-9f8d-4271-ad98-be7eddd4c7fc");
//
//    @BeforeEach
//    void setup() {
//        inMemoryDatabase = InMemoryDatabase.getInstance();
//        inMemoryDatabase.users.clear();
//
//        inMemoryDatabase.roles.add(new Roles(roleAdminId, "Admin", companyId, false, true, Instant.now(), Instant.now()));
//        inMemoryDatabase.roles.add(new Roles(roleDeveloperId, "Developer", companyId, false, true, Instant.now(), Instant.now()));
//        inMemoryDatabase.roles.add(new Roles(roleProjectDirectorId, "Team Leader", companyId, false, true, Instant.now(), Instant.now()));
//
//        System.out.println("Users count in setup: " + inMemoryDatabase.users.size());
//    }
//
//    @Nested
//    class CreateUser {
//        @Test
//        void shouldCreateUserWithProperData() throws Exception {
////            UUID newUserId = UUID.randomUUID();
//            UsersCreate user = new UsersCreate(
//                    "John Doe",
//                    "jdoe@test.com",
//                    "John Doe",
//                    "Doe",
//                    true,
//                    companyId,
//                    false
//            );
//            String userJson = objectMapper.writeValueAsString(user);
//
//            mockMvc.perform(post("/" + companyId + "/members")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(userJson))
//                    .andExpect(status().isCreated())
//                    .andExpect(jsonPath("$.status").value("success"))
//                    .andExpect(jsonPath("$.data").isMap())
//                    .andExpect(jsonPath("$.data.id").isString())
//                    .andExpect(jsonPath("$.data.name").value("John Doe"))
//                    .andExpect(jsonPath("$.data.email").value("jdoe@test.com"))
//                    .andExpect(jsonPath("$.data.title").value("Doe"))
//                    .andExpect(jsonPath("$.data.isActive").value(true))
//                    .andExpect(jsonPath("$.data.companyId").value(companyId.toString()))
//                    .andExpect(jsonPath("$.data.isOwner").value(false))
//                    .andDo(print());
//
//            mockMvc.perform(get("/" + companyId + "/members"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.status").value("success"))
//                    .andExpect(jsonPath("$.data").isArray())
//                    .andExpect(jsonPath("$.data", hasSize(3)));
//        }
//
//        @Test
//        void shouldReturnErrorIfNameIsBlank() throws Exception {
//            UsersCreate user = new UsersCreate(
//                    "",
//                    "jdoe@test.com",
//                    "John Doe",
//                    "Doe",
//                    true,
//                    companyId,
//                    false
//            );
//            String userJson = objectMapper.writeValueAsString(user);
//            mockMvc.perform(post("/" + companyId + "/members")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(userJson))
//                    .andExpect(status().isBadRequest())
//                    .andExpect(jsonPath("$.status").value("error"))
//                    .andExpect(jsonPath("$.errors[0].message").value("name cannot be blank"))
//                    .andDo(print());
//        }
//
//        @Test
//        void shouldReturnErrorIfNameIsNull() throws Exception {
//            UsersCreate user = new UsersCreate(
//                    null,
//                    "jdoe@test.com",
//                    "John Doe",
//                    "Doe",
//                    true,
//                    companyId,
//                    false
//            );
//            String userJson = objectMapper.writeValueAsString(user);
//            mockMvc.perform(post("/" + companyId + "/members")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(userJson))
//                    .andExpect(status().isBadRequest())
//                    .andExpect(jsonPath("$.status").value("error"))
//                    .andExpect(jsonPath("$.errors[0].message").value("name cannot be blank"))
//                    .andDo(print());
//        }
//
//        @Test
//        void shouldReturnErrorIfEmailIsBlank() throws Exception {
//            UsersCreate user = new UsersCreate(
//                    "John Doe",
//                    "",
//                    "John Doe",
//                    "Doe",
//                    true,
//                    companyId,
//                    false
//            );
//            String userJson = objectMapper.writeValueAsString(user);
//            mockMvc.perform(post("/" + companyId + "/members")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(userJson))
//                    .andExpect(status().isBadRequest())
//                    .andExpect(jsonPath("$.status").value("error"))
//                    .andExpect(jsonPath("$.errors[0].message").value("email cannot be blank"))
//                    .andDo(print());
//        }
//
//        @Test
//        void shouldReturnErrorIfEmailIsNull() throws Exception {
//            UsersCreate user = new UsersCreate(
//                    "John Doe",
//                    null,
//                    "John Doe",
//                    "Doe",
//                    true,
//                    companyId,
//                    false
//            );
//            String userJson = objectMapper.writeValueAsString(user);
//            mockMvc.perform(post("/" + companyId + "/members")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(userJson))
//                    .andExpect(status().isBadRequest())
//                    .andExpect(jsonPath("$.status").value("error"))
//                    .andExpect(jsonPath("$.errors[0].message").value("email cannot be blank"))
//                    .andDo(print());
//        }
//
//        @Test
//        void shouldReturnErrorIfPasswordIsBlank() throws Exception {
//            UsersCreate user = new UsersCreate(
//                    "John Doe",
//                    "jdoe@test.com",
//                    "",
//                    "Doe",
//                    true,
//                    companyId,
//                    false
//            );
//            String userJson = objectMapper.writeValueAsString(user);
//            mockMvc.perform(post("/" + companyId + "/members")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(userJson))
//                    .andExpect(status().isBadRequest())
//                    .andExpect(jsonPath("$.status").value("error"))
//                    .andExpect(jsonPath("$.errors[0].message").value("passwordHash cannot be blank"))
//                    .andDo(print());
//        }
//
//        @Test
//        void shouldReturnErrorIfPasswordIsNull() throws Exception {
//            UsersCreate user = new UsersCreate(
//                    "John Doe",
//                    "jdoe@test.com",
//                    null,
//                    "Doe",
//                    true,
//                    companyId,
//                    false
//            );
//            String userJson = objectMapper.writeValueAsString(user);
//            mockMvc.perform(post("/" + companyId + "/members")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(userJson))
//                    .andExpect(status().isBadRequest())
//                    .andExpect(jsonPath("$.status").value("error"))
//                    .andExpect(jsonPath("$.errors[0].message").value("passwordHash cannot be blank"))
//                    .andDo(print());
//        }
//
//        @Test
//        void shouldReturnErrorIfActiveIsNull() throws Exception {
//            UsersCreate user = new UsersCreate(
//                    "John Doe",
//                    "jdoe@test.com",
//                    "John Doe",
//                    "Doe",
//                    null,
//                    companyId,
//                    false
//            );
//            String userJson = objectMapper.writeValueAsString(user);
//            mockMvc.perform(post("/" + companyId + "/members")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(userJson))
//                    .andExpect(status().isBadRequest())
//                    .andExpect(jsonPath("$.status").value("error"))
//                    .andExpect(jsonPath("$.errors[0].message").value("isActive cannot be null"))
//                    .andDo(print());
//        }
//
//        @Test
//        void shouldReturnErrorIfCompanyIsNull() throws Exception {
//            UsersCreate user = new UsersCreate(
//                    "John Doe",
//                    "jdoe@test.com",
//                    "John Doe",
//                    "Doe",
//                    true,
//                    null,
//                    false
//            );
//            String userJson = objectMapper.writeValueAsString(user);
//            mockMvc.perform(post("/" + companyId + "/members")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(userJson))
//                    .andExpect(status().isBadRequest())
//                    .andExpect(jsonPath("$.status").value("error"))
//                    .andExpect(jsonPath("$.errors[0].message").value("companyId cannot be null"))
//                    .andDo(print());
//        }
//
//        @Test
//        void shouldReturnErrorIfOwnerIsNull() throws Exception {
//            UsersCreate user = new UsersCreate(
//                    "John Doe",
//                    "jdoe@test.com",
//                    "John Doe",
//                    "Doe",
//                    true,
//                    companyId,
//                    null
//            );
//            String userJson = objectMapper.writeValueAsString(user);
//            mockMvc.perform(post("/" + companyId + "/members")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(userJson))
//                    .andExpect(status().isBadRequest())
//                    .andExpect(jsonPath("$.status").value("error"))
//                    .andExpect(jsonPath("$.errors[0].message").value("isOwner cannot be null"))
//                    .andDo(print());
//        }
//    }
//
//    @Nested
//    class UpdateUser {
//        @Test
//        void shouldUpdateNameCorrectly() throws Exception {
//            UsersUpdate uu = new UsersUpdate(
//                    "John Doe",
//                    null,
//                    null,
//                    null,
//                    null,
//                    null,
//                    null
//            );
//            String userJson = objectMapper.writeValueAsString(uu);
//            mockMvc.perform(patch("/" + companyId + "/members/" + user1)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(userJson))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.status").value("success"))
//                    .andExpect(jsonPath("$.data").isMap())
//                    .andExpect(jsonPath("$.data.id").value(user1.toString()))
//                    .andExpect(jsonPath("$.data.name").value("John Doe"))
//                    .andExpect(jsonPath("$.data.email").value("u1@test.com"))
//                    .andDo(print());
//        }
//
//        @Test
//        void shouldNotUpdateNameIfBlank() throws Exception {
//            UsersUpdate uu = new UsersUpdate(
//                    "",
//                    null,
//                    null,
//                    null,
//                    null,
//                    null,
//                    null
//            );
//            String userJson = objectMapper.writeValueAsString(uu);
//            mockMvc.perform(patch("/" + companyId + "/members/" + user1)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(userJson))
//                    .andExpect(status().isBadRequest())
//                    .andExpect(jsonPath("$.status").value("error"))
//                    .andExpect(jsonPath("$.errors[0].message").value("name must not be empty or whitespace"))
//                    .andDo(print());
//        }
//
//        @Test
//        void shouldUpdateEmailCorrectly() throws Exception {
//            UsersUpdate uu = new UsersUpdate(
//                    null,
//                    "jdoe@test.com",
//                    null,
//                    null,
//                    null,
//                    null,
//                    null
//            );
//            String userJson = objectMapper.writeValueAsString(uu);
//            mockMvc.perform(patch("/" + companyId + "/members/" + user1)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(userJson))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.status").value("success"))
//                    .andExpect(jsonPath("$.data").isMap())
//                    .andExpect(jsonPath("$.data.id").value(user1.toString()))
//                    .andExpect(jsonPath("$.data.name").value("User 1"))
//                    .andExpect(jsonPath("$.data.email").value("jdoe@test.com"))
//                    .andDo(print());
//        }
//
//        @Test
//        void shouldNotUpdateEmailIfBlank() throws Exception {
//            UsersUpdate uu = new UsersUpdate(
//                    null,
//                    "",
//                    null,
//                    null,
//                    null,
//                    null,
//                    null
//            );
//            String userJson = objectMapper.writeValueAsString(uu);
//            mockMvc.perform(patch("/" + companyId + "/members/" + user1)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(userJson))
//                    .andExpect(status().isBadRequest())
//                    .andExpect(jsonPath("$.status").value("error"))
//                    .andExpect(jsonPath("$.errors[0].message").value("email must not be empty or whitespace"))
//                    .andDo(print());
//        }
//
//        @Test
//        void shouldNotUpdateEmailIfNotEmail() throws Exception {
//            UsersUpdate uu = new UsersUpdate(
//                    null,
//                    "abc",
//                    null,
//                    null,
//                    null,
//                    null,
//                    null
//            );
//            String userJson = objectMapper.writeValueAsString(uu);
//            mockMvc.perform(patch("/" + companyId + "/members/" + user1)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(userJson))
//                    .andExpect(status().isBadRequest())
//                    .andExpect(jsonPath("$.status").value("error"))
//                    .andExpect(jsonPath("$.errors[0].message").value("email must be a valid email address"))
//                    .andDo(print());
//        }
//
//
//        @Test
//        void shouldUpdatePasswordCorrectly() throws Exception {
//            UsersUpdate uu = new UsersUpdate(
//                    null,
//                    null,
//                    "newpassword",
//                    null,
//                    null,
//                    null,
//                    null
//            );
//            String userJson = objectMapper.writeValueAsString(uu);
//            mockMvc.perform(patch("/" + companyId + "/members/" + user1)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(userJson))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.status").value("success"))
//                    .andExpect(jsonPath("$.data").isMap())
//                    .andExpect(jsonPath("$.data.id").value(user1.toString()))
//                    .andExpect(jsonPath("$.data.name").value("User 1"))
//                    .andDo(print());
//        }
//
//        @Test
//        void shouldUpdateIsActiveCorrectly() throws Exception {
//            UsersUpdate uu = new UsersUpdate(
//                    null,
//                    null,
//                    null,
//                    null,
//                    false,
//                    null,
//                    null
//            );
//            String userJson = objectMapper.writeValueAsString(uu);
//            mockMvc.perform(patch("/" + companyId + "/members/" + user1)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(userJson))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.status").value("success"))
//                    .andExpect(jsonPath("$.data").isMap())
//                    .andExpect(jsonPath("$.data.id").value(user1.toString()))
//                    .andExpect(jsonPath("$.data.name").value("User 1"))
//                    .andDo(print());
//        }
//    }
//
//    @Nested
//    class DeleteUser {
//        @Test
//        void deleteUserCorrectly() throws Exception {
//            mockMvc.perform(delete("/" + companyId + "/members/" + user2))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.status").value("success"))
//                    .andExpect(jsonPath("$.data").value("User deleted"))
//                    .andDo(print());
//
//            mockMvc.perform(get("/" + companyId + "/members"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.status").value("success"))
//                    .andExpect(jsonPath("$.data").isArray())
//                    .andExpect(jsonPath("$.data", hasSize(1)));
//        }
//    }
//
//    @Nested
//    class GetUsers {
//        @Test
//        void getAllUsersOfCompanyCorrectly() throws Exception {
//            mockMvc.perform(get("/" + companyId + "/members"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.status").value("success"))
//                    .andExpect(jsonPath("$.data").isArray())
//                    .andDo(print());
//        }
//    }
}
