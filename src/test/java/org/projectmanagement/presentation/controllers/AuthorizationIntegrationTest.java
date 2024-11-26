package org.projectmanagement.presentation.controllers;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.projectmanagement.application.dto.users.UsersLogin;
import org.projectmanagement.presentation.config.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthorizationIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Value("${JWT_SECRET}")
    private String jwtSecret;

    private UserDetailsService userDetailsService;

    private final UUID companyId = UUID.fromString("3bbb31a7-0915-4d77-b185-e939a5b9cd38");
    private final UUID ownerId = UUID.fromString("156de810-1339-469d-9611-8443cdda88d1");
    private final UUID companyManagerId = UUID.fromString("acd9c8ef-87f3-4eae-bd92-238b01b746b6");
    private final UUID workspaceManager1Id = UUID.fromString("f980e928-2ba4-47c5-abaa-cc675be24671");
    private final UUID workspaceManager2Id = UUID.fromString("7f28dd30-549a-41af-bd42-1239651fe4f5");
    private final UUID member1Id = UUID.fromString("4a7d763b-9ec7-46a0-9861-9c0dfb8e7129");
    private final UUID member2Id = UUID.fromString("745b3885-cb08-4076-b97e-626c56e10e0f");
    private final UUID member3Id = UUID.fromString("f3b3b3b3-3b3b-3b3b-3b3b-3b3b3b3b3b3b");
    private final UUID member4Id = UUID.fromString("4b4b4b4b-4b4b-4b4b-4b4b-4b4b4b4b4b4b");

    private final UUID adminRoleId = UUID.fromString("0c3bc98e-f22b-42ae-875e-0ab066ecd327");
    private final UUID companyManagerRoleId = UUID.fromString("1e8cdfaa-4bd4-4111-866f-6292f26d97f1");
    private final UUID workspaceManagerRoleId = UUID.fromString("22bd7e77-7830-46c7-943a-c706b174c390");
    private final UUID memberRoleId = UUID.fromString("238bb464-40fb-4bdb-8d10-d2e97c4849a7");

    private final UUID workspace1Id = UUID.fromString("caa47ad6-6a0c-4733-82cc-af51b5412d94");
    private final UUID workspace2Id = UUID.fromString("de7968b1-762a-4ea6-b6ec-cab609005012");

    private final UUID project1Id = UUID.fromString("d9b2efea-9447-49a2-b904-f3be8261f8d2");
    private final UUID project2Id = UUID.fromString("4863b61b-f2ac-488d-b80f-68748cd0978b");

    private static String ownerEmail = "owner@fs19java.com";

    @Autowired
    private JwtHelper jwtHelper;

    private static String adminToken;

    @Nested
    class AdminAuthorization {
        // Admin should be able to access all resources
        @BeforeAll
        public static void setUp(@Autowired JwtHelper jwtHelper) {
            adminToken = jwtHelper.generateToken(new UsersLogin(ownerEmail, "12345678"));
        }

        @Test
        public void shouldBeAbleToGetAllUsers() throws Exception {
            mockMvc.perform(get("/" + companyId + "/members")
                    .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andDo(print());
        }

        @Test
        public void shouldBeAbleToGetAllWorkspaces() throws Exception {
            mockMvc.perform(get("/" + companyId + "/workspaces")
                    .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andDo(print());
        }
    }
}
