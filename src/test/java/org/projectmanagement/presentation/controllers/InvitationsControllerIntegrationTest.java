package org.projectmanagement.presentation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.projectmanagement.application.dto.invitations.InvitationsCreate;
import org.projectmanagement.application.dto.users.UsersLogin;
import org.projectmanagement.application.exceptions.AppMessage;
import org.projectmanagement.domain.entities.Roles;
import org.projectmanagement.domain.services.AuthService;
import org.projectmanagement.presentation.response.GlobalResponse;
import org.projectmanagement.test_data_factories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class InvitationsControllerIntegrationTest {

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

    private UUID userId;
    private UUID userId2;
    private String jwtToken;
    private String jwtToken2;
    private UUID companyId;
    private UUID adminRoleId;
    private UUID workspaceId;

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
    class PostInvitation {

        @Test
        void sendInvitation() throws Exception {
            Instant expirationDate = Instant.now().plus(1, ChronoUnit.DAYS);
            InvitationsCreate invitationsCreate = new InvitationsCreate("testuserinvitationemail@test.com",
                    workspaceId.toString(),
                    adminRoleId.toString(),
                    expirationDate, false);
            mockMvc.perform(post("/" + companyId + "/invitations/")
                            .header("Authorization", "Bearer " + jwtToken)
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(invitationsCreate)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.status").value(GlobalResponse.SUCCESS))
                    .andExpect(jsonPath("$.data.userEmail").value(invitationsCreate.userEmail()))
                    .andDo(print());
        }

        @Test
        void shouldNotSendInvitation() throws Exception {
            Instant expirationDate = Instant.now().minus(1, ChronoUnit.DAYS);
            InvitationsCreate invitationsCreate = new InvitationsCreate("testuserinvitationemail@test.com",
                    workspaceId.toString(),
                    adminRoleId.toString(),
                    expirationDate, false);
            mockMvc.perform(post("/" + companyId + "/invitations/")
                            .header("Authorization", "Bearer " + jwtToken)
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(invitationsCreate)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.status").value(GlobalResponse.SUCCESS))
                    .andExpect(jsonPath("$.data.userEmail").value(invitationsCreate.userEmail()))
                    .andDo(print());

            mockMvc.perform(post("/" + companyId + "/invitations/")
                            .header("Authorization", "Bearer " + jwtToken)
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(invitationsCreate)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(GlobalResponse.ERROR))
                    .andExpect(jsonPath("$.errors[0].message").value(AppMessage.INVITATION_ALREADY_SENT.getMessage()))
                    .andDo(print());
        }
    }

    @Nested
    class GetInvitations {

        @Test
        void getInvitations() throws Exception {

            Instant expirationDate = Instant.now().minus(1, ChronoUnit.DAYS);
            InvitationsCreate invitationsCreate = new InvitationsCreate("testuserinvitationemail@test.com",
                    workspaceId.toString(),
                    adminRoleId.toString(),
                    expirationDate, false);

            InvitationsCreate invitationsCreate2 = new InvitationsCreate("testuserinvitationemai2l@test.com",
                    workspaceId.toString(),
                    adminRoleId.toString(),
                    expirationDate, false);

            mockMvc.perform(post("/" + companyId + "/invitations/")
                            .header("Authorization", "Bearer " + jwtToken)
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(invitationsCreate)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.status").value(GlobalResponse.SUCCESS))
                    .andExpect(jsonPath("$.data.userEmail").value(invitationsCreate.userEmail()))
                    .andDo(print());

            mockMvc.perform(post("/" + companyId + "/invitations/")
                            .header("Authorization", "Bearer " + jwtToken)
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(invitationsCreate2)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.status").value(GlobalResponse.SUCCESS))
                    .andExpect(jsonPath("$.data.userEmail").value(invitationsCreate2.userEmail()))
                    .andDo(print());

            mockMvc.perform(get("/" + companyId + "/invitations/")
                            .header("Authorization", "Bearer " + jwtToken)
                            .contentType("application/json"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(GlobalResponse.SUCCESS))
                    .andExpect(jsonPath("$.data.length()").value(2))
                    .andDo(print());
        }


        @Test
        void shouldNotGetInvitations() throws Exception {
            mockMvc.perform(get("/" + companyId + "/invitations/")
                            .header("Authorization", "Bearer " + jwtToken2)
                            .contentType("application/json"))
                    .andExpect(status().isForbidden())
                    .andDo(print());
        }

    }

    @Nested
    class PutInvitation {

        @Test
        void refreshInvitation() throws Exception {
            Instant expirationDate = Instant.now().minus(1, ChronoUnit.DAYS);
            InvitationsCreate invitationsCreate = new InvitationsCreate("testuserinvitationemail@test.com",
                    workspaceId.toString(),
                    adminRoleId.toString(),
                    expirationDate, false);

            MvcResult result = mockMvc.perform(post("/" + companyId + "/invitations/")
                            .header("Authorization", "Bearer " + jwtToken)
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(invitationsCreate)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.status").value(GlobalResponse.SUCCESS))
                    .andExpect(jsonPath("$.data.userEmail").value(invitationsCreate.userEmail()))
                    .andDo(print())
                    .andReturn();

            String id = JsonPath.read(result.getResponse().getContentAsString(), "$.data.id");
            MvcResult updateResult = mockMvc.perform(put("/" + companyId + "/invitations/" + id)
                            .header("Authorization", "Bearer " + jwtToken)
                            .param("invitationId", id)
                            .param("extend", "2"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(GlobalResponse.SUCCESS))
                    .andDo(print()).andReturn();
            Instant updateExpiredDate = Instant.parse(JsonPath.read(updateResult.getResponse().getContentAsString(), "$.data.expiredAt"));
            assertTrue(ChronoUnit.DAYS.between(expirationDate, updateExpiredDate) >= 2);
        }

        @Test
        void shouldNotExtendInvitation() throws Exception {

            Instant expirationDate = Instant.now().plus(1, ChronoUnit.DAYS);
            InvitationsCreate invitationsCreate = new InvitationsCreate("testuserinvitationemail@test.com",
                    workspaceId.toString(),
                    adminRoleId.toString(),
                    expirationDate, false);

            MvcResult result = mockMvc.perform(post("/" + companyId + "/invitations/")
                            .header("Authorization", "Bearer " + jwtToken)
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(invitationsCreate)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.status").value(GlobalResponse.SUCCESS))
                    .andExpect(jsonPath("$.data.userEmail").value(invitationsCreate.userEmail()))
                    .andDo(print())
                    .andReturn();

            String id = JsonPath.read(result.getResponse().getContentAsString(), "$.data.id");
            mockMvc.perform(put("/" + companyId + "/invitations/" + id)
                            .header("Authorization", "Bearer " + jwtToken)
                            .param("invitationId", id)
                            .param("extend", "2"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(GlobalResponse.ERROR))
                    .andExpect(jsonPath("$.errors[0].message").value(AppMessage.INVITATION_STILL_VALID.getMessage()))
                    .andDo(print()).andReturn();
        }
    }

    @Nested
    class DeleteInvitation {

        @Test
        void revokeInvitation() throws Exception {
            Instant expirationDate = Instant.now().plus(1, ChronoUnit.DAYS);
            InvitationsCreate invitationsCreate = new InvitationsCreate("testuserinvitationemail@test.com",
                    workspaceId.toString(),
                    adminRoleId.toString(),
                    expirationDate, false);

            MvcResult result = mockMvc.perform(post("/" + companyId + "/invitations/")
                            .header("Authorization", "Bearer " + jwtToken)
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(invitationsCreate)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.status").value(GlobalResponse.SUCCESS))
                    .andExpect(jsonPath("$.data.userEmail").value(invitationsCreate.userEmail()))
                    .andDo(print())
                    .andReturn();

            String id = JsonPath.read(result.getResponse().getContentAsString(), "$.data.id");

            mockMvc.perform(delete("/" + companyId + "/invitations/")
                            .header("Authorization", "Bearer " + jwtToken)
                            .param("email",invitationsCreate.userEmail())
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(GlobalResponse.SUCCESS))
                    .andExpect(jsonPath("$.data").value("true"))
                    .andDo(print());
        }

        @Test
        void shouldNotRevokeInvitation() throws Exception {
            Instant expirationDate = Instant.now().plus(1, ChronoUnit.DAYS);
            InvitationsCreate invitationsCreate = new InvitationsCreate("testuserinvitationemail@test.com",
                    workspaceId.toString(),
                    adminRoleId.toString(),
                    expirationDate, false);

            MvcResult result = mockMvc.perform(post("/" + companyId + "/invitations/")
                            .header("Authorization", "Bearer " + jwtToken)
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(invitationsCreate)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.status").value(GlobalResponse.SUCCESS))
                    .andExpect(jsonPath("$.data.userEmail").value(invitationsCreate.userEmail()))
                    .andDo(print())
                    .andReturn();

            String id = JsonPath.read(result.getResponse().getContentAsString(), "$.data.id");

            mockMvc.perform(delete("/" + companyId + "/invitations/")
                            .header("Authorization", "Bearer " + jwtToken)
                            .param("email",invitationsCreate.userEmail())
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(GlobalResponse.SUCCESS))
                    .andExpect(jsonPath("$.data").value("true"))
                    .andDo(print());

            mockMvc.perform(delete("/" + companyId + "/invitations/")
                            .header("Authorization", "Bearer " + jwtToken)
                            .param("email",invitationsCreate.userEmail())
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(GlobalResponse.ERROR))
                    .andExpect(jsonPath("$.errors[0].message").value(AppMessage.INVITATION_NOT_FOUND.getMessage()))
                    .andDo(print());
        }
    }

}
