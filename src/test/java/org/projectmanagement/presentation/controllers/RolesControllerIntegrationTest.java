package org.projectmanagement.presentation.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.projectmanagement.application.dto.roles.RolesCreate;
import org.projectmanagement.domain.entities.Roles;
import org.projectmanagement.infrastructure.InMemoryDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.http.MediaType;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class RolesControllerIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    InMemoryDatabase inMemoryDatabase;
    UUID companyId = UUID.fromString("b541ade4-9cfa-4664-b9e3-d9923ae02fb4");
    UUID roleAdminId = UUID.fromString("7b149139-6b39-4e5c-9e24-70c092df4a5d");
    UUID roleDeveloperId = UUID.fromString("78b4fe40-5f93-40ef-9095-7b25c7bb62ff");
    UUID roleProjectDirectorId = UUID.fromString("fed18c17-dc47-45f4-8b59-aafc04089a58");

    @BeforeEach
    void setUp() {
        inMemoryDatabase = InMemoryDatabase.getInstance();
        inMemoryDatabase.roles.clear();

        inMemoryDatabase.roles.add(new Roles(roleAdminId, "Admin", companyId, false, true, Instant.now(), Instant.now()));
        inMemoryDatabase.roles.add(new Roles(roleDeveloperId, "Developer", companyId, false, true, Instant.now(), Instant.now()));
        inMemoryDatabase.roles.add(new Roles(roleProjectDirectorId, "Team Leader", companyId, false, true, Instant.now(), Instant.now()));

        System.out.println("Roles count in setup: " + inMemoryDatabase.roles.size());
    }

    @Nested
    class GetRoles {
        @Test
        void getAllRolesCorrectly() throws Exception {
            mockMvc.perform(get("/"+ companyId + "/roles"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data", hasSize(4)))
                    .andDo(print());
        }
    }

    @Nested
    class PostRoles {
        @Test
        void shouldCreateRoleNormallyWithProperData() throws Exception{
            RolesCreate newRole = new RolesCreate("Project Manager", companyId);
            String roleJson = objectMapper.writeValueAsString(newRole);

            mockMvc.perform(post("/" + companyId + "/roles")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(roleJson))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.data.name").value("Project Manager"))
                    .andDo(print());
        }

        @Test
        void shouldNotCreateRoleWithBlankName() throws Exception{
            RolesCreate newRole = new RolesCreate("", companyId);
            String roleJson = objectMapper.writeValueAsString(newRole);

            mockMvc.perform(post("/" + companyId + "/roles")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(roleJson))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.data").value(nullValue()))
                    .andExpect(jsonPath("$.errors[0].message").value("name cannot be blank"))
                    .andDo(print());
        }

        @Test
        void shouldNotCreateRoleWithNullCompanyId() throws Exception{
            RolesCreate newRole = new RolesCreate("Project Manager", null);
            String roleJson = objectMapper.writeValueAsString(newRole);

            mockMvc.perform(post("/" + companyId + "/roles")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(roleJson))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.data").value(nullValue()))
                    .andExpect(jsonPath("$.errors[0].message").value("companyId cannot be null"))
                    .andDo(print());
        }

        @Test
        void shouldNotCreateRoleWithExistingName() throws Exception{
            RolesCreate newRole = new RolesCreate("Admin", companyId);
            String roleJson = objectMapper.writeValueAsString(newRole);

            mockMvc.perform(post("/" + companyId + "/roles")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(roleJson))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.data").value(nullValue()))
                    .andDo(print());
        }
    }

    @Nested
    class PutRoles {
        @Test
        void shouldUpdateRoleNameNormally() throws Exception{
            RolesCreate updatedRole = new RolesCreate("Project Director", companyId);
            String updatedRoleJson = objectMapper.writeValueAsString(updatedRole);

            mockMvc.perform(put("/" + companyId + "/roles/" + roleDeveloperId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(updatedRoleJson))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("success"))
                    .andExpect(jsonPath("$.data.name").value("Project Director"))
                    .andDo(print());
        }

        @Test
        void shouldNotUpdateRoleWithBlankName() throws Exception{
            RolesCreate updatedRole = new RolesCreate("", companyId);
            String updatedRoleJson = objectMapper.writeValueAsString(updatedRole);

            mockMvc.perform(put("/" + companyId + "/roles/" + roleDeveloperId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(updatedRoleJson))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.data").value(nullValue()))
                    .andExpect(jsonPath("$.errors[0].message").value("name cannot be blank"))
                    .andDo(print());
        }

        @Test
        void shouldNotUpdateRoleWithNullName() throws Exception {
            RolesCreate updatedRole = new RolesCreate(null, companyId);
            String updatedRoleJson = objectMapper.writeValueAsString(updatedRole);

            mockMvc.perform(put("/" + companyId + "/roles/" + roleDeveloperId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(updatedRoleJson))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.data").value(nullValue()))
                    .andExpect(jsonPath("$.errors[0].message").value("name cannot be blank"))
                    .andDo(print());
        }
    }
}
