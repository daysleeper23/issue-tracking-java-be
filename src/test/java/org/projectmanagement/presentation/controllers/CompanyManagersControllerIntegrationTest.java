package org.projectmanagement.presentation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.projectmanagement.domain.entities.Roles;
import org.projectmanagement.infrastructure.InMemoryDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
//@ExtendWith((SpringExtension.class))
public class CompanyManagersControllerIntegrationTest {

    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    InMemoryDatabase inMemoryDatabase;
    UUID companyId = UUID.fromString("b541ade4-9cfa-4664-b9e3-d9923ae02fb4");
    UUID adminUserId = UUID.fromString("7b149139-6b39-4e5c-9e24-70c092df4a5d");
    UUID CompanyManagerId = UUID.fromString("78b4fe40-5f93-40ef-9095-7b25c7bb62ff");

    @BeforeEach
    void setUp() {
        inMemoryDatabase = InMemoryDatabase.getInstance();
        inMemoryDatabase.roles.clear();

//        inMemoryDatabase.roles.add(new Roles(roleAdminId, "Admin", companyId, false, true, Instant.now(), Instant.now()));
//        inMemoryDatabase.roles.add(new Roles(roleDeveloperId, "Developer", companyId, false, true, Instant.now(), Instant.now()));
//        inMemoryDatabase.roles.add(new Roles(roleProjectDirectorId, "Team Leader", companyId, false, true, Instant.now(), Instant.now()));

        System.out.println("Roles count in setup: " + inMemoryDatabase.roles.size());
    }
}
