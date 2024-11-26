package org.projectmanagement.presentation.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.projectmanagement.application.dto.companies.Company;
import org.projectmanagement.application.dto.users.UsersLogin;
import org.projectmanagement.application.exceptions.AppMessage;
import org.projectmanagement.domain.entities.Companies;
import org.projectmanagement.domain.services.AuthService;
import org.projectmanagement.presentation.response.GlobalResponse;
import org.projectmanagement.test_data_factories.CompaniesDataFactory;
import org.projectmanagement.test_data_factories.CompanyManagersDataFactory;
import org.projectmanagement.test_data_factories.RolesDataFactory;
import org.projectmanagement.test_data_factories.UsersDataFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.core.IsNull.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class CompaniesControllerIntegrationTest {

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

    private String jwtToken;
    private String jwtToken2;
    private String jwtToken3;
    private UUID userId;
    private UUID testUpdateCompanyId;
    private UUID testUserIdUpdate;
    private UUID testUserIdUpdate2;
    private UUID superAdminRoleId;
    private UUID companyManagerRoleId;


    @BeforeEach
    void setUp() {
        userId = usersDataFactory.createNonOwnerUser(null);
        UsersLogin userLogin = new UsersLogin("testuser2@example.com", "hashedpassword");
        jwtToken = authService.authenticate(userLogin);

        testUpdateCompanyId = companiesDataFactory.createCompany();
        testUserIdUpdate = usersDataFactory.createOwnerUser(testUpdateCompanyId,"t@test.com", "hashedpassword");
        testUserIdUpdate2 = usersDataFactory.createNonOwnerUser(testUpdateCompanyId,"t2@test.com", "hashedpassword");
        UsersLogin t2 = new UsersLogin("t@test.com", "hashedpassword");
        jwtToken2 = authService.authenticate(t2);
        superAdminRoleId = rolesDataFactory.createRoleWithAllPermissions("Super Admin", testUpdateCompanyId, false);
        companyManagersDataFactory.createCompanyManager(testUserIdUpdate, superAdminRoleId, testUpdateCompanyId);
        UsersLogin t3 = new UsersLogin("t2@test.com", "hashedpassword");
        jwtToken3 = authService.authenticate(t3);
    }

    @AfterEach
    void cleanUp() {
        usersDataFactory.deleteAll();
        companiesDataFactory.deleteAll();
    }

    @Nested
    class GetCompanies {
        @Test
        void getCompany() throws Exception{
            mockMvc.perform(get("/companies/" + testUpdateCompanyId)
                            .header("Authorization", "Bearer " + jwtToken2)
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(GlobalResponse.SUCCESS))
                    .andExpect(jsonPath("$.data.id").value(testUpdateCompanyId.toString()))
                    .andExpect(jsonPath("$.data.ownerId").value(testUserIdUpdate.toString()))
                    .andDo(print());
        }

        @Test
        void shouldNotGetCompany() throws Exception{
            mockMvc.perform(get("/companies/" + testUpdateCompanyId)
                            .header("Authorization", "Bearer " + jwtToken)
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isForbidden())
                    .andDo(print());
        }

        @Test
        void shouldGetCompanyNotFound() throws Exception{
            mockMvc.perform(get("/companies/" + UUID.randomUUID())
                            .header("Authorization", "Bearer " + jwtToken2)
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isNotFound())
                    .andDo(print());
        }
    }

    @Nested
    class PostCompany {

        @Test
        void createCompany() throws Exception {
            Company company = new Company("Test Company", "Test Description", userId.toString());
            mockMvc.perform(post("/companies")
                            .header("Authorization", "Bearer " + jwtToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(company))
                    )
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.status").value(GlobalResponse.SUCCESS))
                    .andExpect(jsonPath("$.data.name").value(company.name()))
                    .andExpect(jsonPath("$.data.description").value(company.description()))
                    .andExpect(jsonPath("$.data.ownerId").value(userId.toString()))
                    .andDo(print());
        }

        @Test
        void shouldNotCreateCompanyWhenUserIsAlreadyJoinedCompany() throws Exception {
            Company company = new Company("Test Company", "Test Description", userId.toString());
            mockMvc.perform(post("/companies")
                            .header("Authorization", "Bearer " + jwtToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(company))
                    )
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.status").value(GlobalResponse.SUCCESS))
                    .andExpect(jsonPath("$.data.name").value(company.name()))
                    .andExpect(jsonPath("$.data.description").value(company.description()))
                    .andExpect(jsonPath("$.data.ownerId").value(userId.toString()))
                    .andDo(print());

            mockMvc.perform(post("/companies")
                            .header("Authorization", "Bearer " + jwtToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(company))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(GlobalResponse.ERROR))
                    .andExpect(jsonPath("$.data").value(nullValue()))
                    .andExpect(jsonPath("$.errors[0].message").value("User already joined a company"))
                    .andDo(print());
        }
    }

    @Nested
    class PutCompany {
        @Test
        void updateCompany() throws Exception {
            Company company = new Company("Test update name", "Test update description", userId.toString());
            mockMvc.perform(put("/companies/" + testUpdateCompanyId)
                            .header("Authorization", "Bearer " + jwtToken2)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(company))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(GlobalResponse.SUCCESS))
                    .andExpect(jsonPath("$.data.name").value(company.name()))
                    .andExpect(jsonPath("$.data.description").value(company.description()))
                    .andExpect(jsonPath("$.data.ownerId").value(testUserIdUpdate.toString()))
                    .andDo(print());
        }

        @Test
        void shouldNotUpdateCompany() throws Exception {
            Company company = new Company("Test update name", "Test update description", userId.toString());
            mockMvc.perform(put("/companies/" + testUpdateCompanyId)
                            .header("Authorization", "Bearer " + jwtToken3)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(company))
                    )
                    .andExpect(status().isForbidden())
                    .andDo(print());
        }

        @Test
        void shouldNotUpdateCompanyNotFound() throws Exception {
            Company company = new Company("Test update name", "Test update description", userId.toString());
            mockMvc.perform(put("/companies/" + UUID.randomUUID())
                            .header("Authorization", "Bearer " + jwtToken2)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(company))
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(GlobalResponse.ERROR))
                    .andExpect(jsonPath("$.errors[0].message").value(AppMessage.COMPANY_NOT_FOUND.getMessage()))
                    .andDo(print());
        }

        @Test
        void shouldNotUpdateWhenNoChanges() throws Exception {
            Company company = new Company("Test Company", "Test Description", userId.toString());
            mockMvc.perform(put("/companies/" + testUpdateCompanyId)
                            .header("Authorization", "Bearer " + jwtToken2)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsBytes(company))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(GlobalResponse.ERROR))
                    .andExpect(jsonPath("$.data").value(nullValue()))
                    .andExpect(jsonPath("$.errors[0].message").value(AppMessage.NO_CHANGE.getMessage()))
                    .andDo(print());
        }

    }
}
