package org.projectmanagement.presentation.config;

import lombok.extern.log4j.Log4j2;
import org.projectmanagement.domain.repository.PermissionsRepoJpa;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Log4j2
@Configuration
@Component
public class DataInitializer {
    private final JdbcTemplate jdbcTemplate;

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
    private final UUID wmr_wm_1Id = UUID.fromString("259c7860-9652-4e60-94db-ec497788d2b5");
    private final UUID wmr_m1_1Id = UUID.fromString("6993455f-8ef8-4f3e-bef8-9db3bcb99ba4");
    private final UUID wmr_m2_1Id = UUID.fromString("20af44ce-0a3b-40ce-842e-12b5a53bf059");
    private final UUID wmr_wm_2Id = UUID.fromString("698c4a40-de56-4951-9d97-f92082a6744e");
    private final UUID wmr_m1_2Id = UUID.fromString("65c30158-e11c-42d2-a9b9-0834cb37b673");
    private final UUID wmr_m2_2Id = UUID.fromString("5b066a05-fee4-447f-b85e-6d7625eee310");

    private final UUID project1Id = UUID.fromString("d9b2efea-9447-49a2-b904-f3be8261f8d2");
    private final UUID project2Id = UUID.fromString("4863b61b-f2ac-488d-b80f-68748cd0978b");

    public DataInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Bean
    CommandLineRunner initializeData(PermissionsRepoJpa permissionsRepository) {
        return args -> {
            initializeCompanies();
            initializeUsers();
            initializeRolesPermissions();
            initializeWorkspacesAndMemberRoles();
            initializeProjectsAndMembers();
        };
    }

    public void initializeCompanies() {
        String sql = "DELETE FROM companies WHERE id = ?";
        jdbcTemplate.update(sql, companyId);

        sql = "INSERT INTO companies (id, name, description, owner_id, created_at, updated_at)" +
            " VALUES (?, ?, ?, ?, ?, ?)";

        // Example Data
        jdbcTemplate.update(sql, companyId, "FS19Java", "FS19Java Company", ownerId, Timestamp.from(Instant.now()), Timestamp.from(Instant.now()));
    }

    public void initializeUsers() {
        String sql = "DELETE FROM users WHERE company_id = ?";
        jdbcTemplate.update(sql, companyId);

        sql = "INSERT INTO users (id, created_at, updated_at, company_id, email, is_active, is_deleted, is_owner, name, password_hash)" +
            " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        //Owner
        jdbcTemplate.update(sql, ownerId, Timestamp.from(Instant.now()), Timestamp.from(Instant.now()), companyId, "owner@fs19java.com", true, false, true, "Owner", "$2a$10$8MN2J34VVigMIbpPWiO7Z..N/cPP/Q6A26aQxYJBXoy4qSzMx8JPS");

        //Company Manager
        jdbcTemplate.update(sql, companyManagerId, Timestamp.from(Instant.now()), Timestamp.from(Instant.now()), companyId, "company_manager@fs19java.com", true, false, false, "Company Manager", "$2a$10$b27Qk0HIHMo9XAxc1z068ODJEqKHSa3LO0prAQ7WZs4b/JDxQ3NjS");

        //Workspace Manager
        jdbcTemplate.update(sql, workspaceManager1Id, Timestamp.from(Instant.now()), Timestamp.from(Instant.now()), companyId, "workspace_manager1@fs19java.com", true, false, false, "Workspace Manager 1", "$2a$10$b27Qk0HIHMo9XAxc1z068ODJEqKHSa3LO0prAQ7WZs4b/JDxQ3NjS");
        jdbcTemplate.update(sql, workspaceManager2Id, Timestamp.from(Instant.now()), Timestamp.from(Instant.now()), companyId, "workspace_manager2@fs19java.com", true, false, false, "Workspace Manager 2", "$2a$10$b27Qk0HIHMo9XAxc1z068ODJEqKHSa3LO0prAQ7WZs4b/JDxQ3NjS");

        //Member
        jdbcTemplate.update(sql, member1Id, Timestamp.from(Instant.now()), Timestamp.from(Instant.now()), companyId, "member1@fs19java.com", true, false, false, "Member 1", "$2a$10$b27Qk0HIHMo9XAxc1z068ODJEqKHSa3LO0prAQ7WZs4b/JDxQ3NjS");
        jdbcTemplate.update(sql, member2Id, Timestamp.from(Instant.now()), Timestamp.from(Instant.now()), companyId, "member2@fs19java.com", true, false, false, "Member 2", "$2a$10$b27Qk0HIHMo9XAxc1z068ODJEqKHSa3LO0prAQ7WZs4b/JDxQ3NjS");
        jdbcTemplate.update(sql, member3Id, Timestamp.from(Instant.now()), Timestamp.from(Instant.now()), companyId, "member3@fs19java.com", true, false, false, "Member 3", "$2a$10$b27Qk0HIHMo9XAxc1z068ODJEqKHSa3LO0prAQ7WZs4b/JDxQ3NjS");
        jdbcTemplate.update(sql, member4Id, Timestamp.from(Instant.now()), Timestamp.from(Instant.now()), companyId, "member4@fs19java.com", true, false, false, "Member 4", "$2a$10$b27Qk0HIHMo9XAxc1z068ODJEqKHSa3LO0prAQ7WZs4b/JDxQ3NjS");
    }

    public void initializeRolesPermissions() {
        String sql = "DELETE FROM roles WHERE company_id = ?";
        jdbcTemplate.update(sql, companyId);

        sql = "INSERT INTO roles (id, name, is_system_role, is_deleted, company_id, created_at, updated_at)" +
            " VALUES (?, ?, ?, ?, ?, ?, ?)";

        // Roles Data
        jdbcTemplate.update(sql, adminRoleId, "Super Admin", true, false, companyId, Timestamp.from(Instant.now()), Timestamp.from(Instant.now()));
        jdbcTemplate.update(sql, companyManagerRoleId, "Company Manager", true, false, companyId, Timestamp.from(Instant.now()), Timestamp.from(Instant.now()));
        jdbcTemplate.update(sql, workspaceManagerRoleId, "Workspace Manager", true, false, companyId, Timestamp.from(Instant.now()), Timestamp.from(Instant.now()));
        jdbcTemplate.update(sql, memberRoleId, "Member", true, false, companyId, Timestamp.from(Instant.now()), Timestamp.from(Instant.now()));

        sql = "DELETE FROM roles_permissions WHERE role_id IN (?, ?, ?, ?)";
        jdbcTemplate.update(sql, adminRoleId, companyManagerRoleId, workspaceManagerRoleId, memberRoleId);

        sql = "DELETE FROM permissions WHERE name <> ?";
        jdbcTemplate.update(sql, "READONLY");

        // Permissions Data
        sql = "INSERT INTO permissions (id, name) VALUES (?, ?)";
        jdbcTemplate.update(sql, UUID.fromString("00f74750-4f78-460e-adc2-70f007fdfd1e"), "COMPANY_READ");
        jdbcTemplate.update(sql, UUID.fromString("281da56c-d00c-460f-be6a-990db44ddc34"), "COMPANY_UPDATE");

        jdbcTemplate.update(sql, UUID.fromString("f255dd47-f282-4aec-ae22-6c44fbf1d933"), "ROLE_CREATE");
        jdbcTemplate.update(sql, UUID.fromString("7173cd35-e3eb-4019-956c-f9e1d07ed628"), "ROLE_READ_ALL");
        jdbcTemplate.update(sql, UUID.fromString("05a1d5ab-06ae-4301-8885-83ef33cf0a9d"), "ROLE_UPDATE_ALL");
        jdbcTemplate.update(sql, UUID.fromString("4272b481-c039-4712-950e-e278aebe62b0"), "ROLE_DELETE_ALL");

        jdbcTemplate.update(sql, UUID.fromString("a3c4ef1f-0118-439f-9161-4378c43b11f9"), "WORKSPACE_CREATE");
        jdbcTemplate.update(sql, UUID.fromString("1e8cdfaa-4bd4-4111-866f-6292f26d97f1"), "WORKSPACE_READ_ONE");
        jdbcTemplate.update(sql, UUID.fromString("8946ddd3-bc4b-4951-8df9-8227e28b9a4e"), "WORKSPACE_READ_ALL");
        jdbcTemplate.update(sql, UUID.fromString("d490bcf9-faef-4230-b575-ba72f3a90f61"), "WORKSPACE_UPDATE_ONE");
        jdbcTemplate.update(sql, UUID.fromString("95e4b214-503d-4c80-bd3d-6f2fad77098f"), "WORKSPACE_UPDATE_ALL");
        jdbcTemplate.update(sql, UUID.fromString("d98f4f30-8cfa-4569-a864-49e1916ee715"), "WORKSPACE_DELETE_ONE");
        jdbcTemplate.update(sql, UUID.fromString("b5994a72-4ab7-44e4-bb42-8667c1e2e9d2"), "WORKSPACE_DELETE_ALL");

        jdbcTemplate.update(sql, UUID.fromString("84c0d22c-81fe-43ec-9978-5e3638b49ab7"), "PROJECT_CREATE");
        jdbcTemplate.update(sql, UUID.fromString("e6894dd8-1aa2-4755-a179-8bde5f087f15"), "PROJECT_READ_ALL");
        jdbcTemplate.update(sql, UUID.fromString("de27469d-69bd-46d9-bf10-03290221d8ff"), "PROJECT_READ_ONE");
        jdbcTemplate.update(sql, UUID.fromString("7508cdf3-b617-454a-a106-047438959599"), "PROJECT_UPDATE_ALL");
        jdbcTemplate.update(sql, UUID.fromString("6c7b8378-a952-47a4-b9c5-fb7c2a090541"), "PROJECT_UPDATE_ONE");
        jdbcTemplate.update(sql, UUID.fromString("2b86782c-90ec-4f20-a4d0-fd20972b59ef"), "PROJECT_DELETE_ONE");
        jdbcTemplate.update(sql, UUID.fromString("83965484-5006-4d70-b963-a20bebf2dc3e"), "PROJECT_DELETE_ALL");

        jdbcTemplate.update(sql, UUID.fromString("1e72d950-3e30-4a79-b705-a04d1157862a"), "USER_UPDATE_ONE");
        jdbcTemplate.update(sql, UUID.fromString("3a99f3f5-a8ee-423c-8bb8-21751aa48c49"), "USER_DELETE_ALL");
//        jdbcTemplate.update(sql, UUID.fromString("47b88087-c0ef-4e64-8200-caaee3559c6e"), "USER_DELETE_ONE");
        jdbcTemplate.update(sql, UUID.fromString("6f982bb7-50ce-4610-8295-19afead33bee"), "USER_UPDATE_ALL");

        sql = "INSERT INTO roles_permissions (id, role_id, permission_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, UUID.randomUUID(), adminRoleId, UUID.fromString("00f74750-4f78-460e-adc2-70f007fdfd1e"));
        jdbcTemplate.update(sql, UUID.randomUUID(), adminRoleId, UUID.fromString("281da56c-d00c-460f-be6a-990db44ddc34"));

        jdbcTemplate.update(sql, UUID.randomUUID(), adminRoleId, UUID.fromString("3a99f3f5-a8ee-423c-8bb8-21751aa48c49"));
        jdbcTemplate.update(sql, UUID.randomUUID(), adminRoleId, UUID.fromString("6f982bb7-50ce-4610-8295-19afead33bee"));

        jdbcTemplate.update(sql, UUID.randomUUID(), adminRoleId, UUID.fromString("f255dd47-f282-4aec-ae22-6c44fbf1d933"));
        jdbcTemplate.update(sql, UUID.randomUUID(), adminRoleId, UUID.fromString("7173cd35-e3eb-4019-956c-f9e1d07ed628"));
        jdbcTemplate.update(sql, UUID.randomUUID(), adminRoleId, UUID.fromString("05a1d5ab-06ae-4301-8885-83ef33cf0a9d"));
        jdbcTemplate.update(sql, UUID.randomUUID(), adminRoleId, UUID.fromString("4272b481-c039-4712-950e-e278aebe62b0"));

        jdbcTemplate.update(sql, UUID.randomUUID(), adminRoleId, UUID.fromString("a3c4ef1f-0118-439f-9161-4378c43b11f9"));
        jdbcTemplate.update(sql, UUID.randomUUID(), adminRoleId, UUID.fromString("8946ddd3-bc4b-4951-8df9-8227e28b9a4e"));
        jdbcTemplate.update(sql, UUID.randomUUID(), adminRoleId, UUID.fromString("95e4b214-503d-4c80-bd3d-6f2fad77098f"));
        jdbcTemplate.update(sql, UUID.randomUUID(), adminRoleId, UUID.fromString("b5994a72-4ab7-44e4-bb42-8667c1e2e9d2"));
        jdbcTemplate.update(sql, UUID.randomUUID(), adminRoleId, UUID.fromString("84c0d22c-81fe-43ec-9978-5e3638b49ab7"));
        jdbcTemplate.update(sql, UUID.randomUUID(), adminRoleId, UUID.fromString("e6894dd8-1aa2-4755-a179-8bde5f087f15"));
        jdbcTemplate.update(sql, UUID.randomUUID(), adminRoleId, UUID.fromString("7508cdf3-b617-454a-a106-047438959599"));
        jdbcTemplate.update(sql, UUID.randomUUID(), adminRoleId, UUID.fromString("83965484-5006-4d70-b963-a20bebf2dc3e"));


        //Company Managers
        jdbcTemplate.update(sql, UUID.randomUUID(), companyManagerRoleId, UUID.fromString("a3c4ef1f-0118-439f-9161-4378c43b11f9"));
        jdbcTemplate.update(sql, UUID.randomUUID(), companyManagerRoleId, UUID.fromString("8946ddd3-bc4b-4951-8df9-8227e28b9a4e"));
        jdbcTemplate.update(sql, UUID.randomUUID(), companyManagerRoleId, UUID.fromString("95e4b214-503d-4c80-bd3d-6f2fad77098f"));
        jdbcTemplate.update(sql, UUID.randomUUID(), companyManagerRoleId, UUID.fromString("b5994a72-4ab7-44e4-bb42-8667c1e2e9d2"));
        jdbcTemplate.update(sql, UUID.randomUUID(), companyManagerRoleId, UUID.fromString("84c0d22c-81fe-43ec-9978-5e3638b49ab7"));
        jdbcTemplate.update(sql, UUID.randomUUID(), companyManagerRoleId, UUID.fromString("e6894dd8-1aa2-4755-a179-8bde5f087f15"));
        jdbcTemplate.update(sql, UUID.randomUUID(), companyManagerRoleId, UUID.fromString("7508cdf3-b617-454a-a106-047438959599"));
        jdbcTemplate.update(sql, UUID.randomUUID(), companyManagerRoleId, UUID.fromString("83965484-5006-4d70-b963-a20bebf2dc3e"));

        jdbcTemplate.update(sql, UUID.randomUUID(), companyManagerRoleId, UUID.fromString("1e72d950-3e30-4a79-b705-a04d1157862a"));

        //Workspace Managers
        jdbcTemplate.update(sql, UUID.randomUUID(), workspaceManagerRoleId, UUID.fromString("a3c4ef1f-0118-439f-9161-4378c43b11f9"));
        jdbcTemplate.update(sql, UUID.randomUUID(), workspaceManagerRoleId, UUID.fromString("1e8cdfaa-4bd4-4111-866f-6292f26d97f1"));
        jdbcTemplate.update(sql, UUID.randomUUID(), workspaceManagerRoleId, UUID.fromString("d490bcf9-faef-4230-b575-ba72f3a90f61"));
        jdbcTemplate.update(sql, UUID.randomUUID(), workspaceManagerRoleId, UUID.fromString("d98f4f30-8cfa-4569-a864-49e1916ee715"));

        jdbcTemplate.update(sql, UUID.randomUUID(), workspaceManagerRoleId, UUID.fromString("84c0d22c-81fe-43ec-9978-5e3638b49ab7"));
        jdbcTemplate.update(sql, UUID.randomUUID(), workspaceManagerRoleId, UUID.fromString("e6894dd8-1aa2-4755-a179-8bde5f087f15"));
        jdbcTemplate.update(sql, UUID.randomUUID(), workspaceManagerRoleId, UUID.fromString("7508cdf3-b617-454a-a106-047438959599"));
        jdbcTemplate.update(sql, UUID.randomUUID(), workspaceManagerRoleId, UUID.fromString("83965484-5006-4d70-b963-a20bebf2dc3e"));

        jdbcTemplate.update(sql, UUID.randomUUID(), workspaceManagerRoleId, UUID.fromString("1e72d950-3e30-4a79-b705-a04d1157862a"));

        //Members
        jdbcTemplate.update(sql, UUID.randomUUID(), memberRoleId, UUID.fromString("6c7b8378-a952-47a4-b9c5-fb7c2a090541"));
        jdbcTemplate.update(sql, UUID.randomUUID(), memberRoleId, UUID.fromString("1e72d950-3e30-4a79-b705-a04d1157862a"));

        //company managers
        sql = "DELETE FROM company_managers WHERE company_id = ?";
        jdbcTemplate.update(sql, companyId);

        sql = "INSERT INTO company_managers (id, user_id, role_id, company_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, UUID.randomUUID(), companyManagerId, companyManagerRoleId, companyId, Timestamp.from(Instant.now()), Timestamp.from(Instant.now()));
        jdbcTemplate.update(sql, UUID.randomUUID(), ownerId, adminRoleId, companyId, Timestamp.from(Instant.now()), Timestamp.from(Instant.now()));
    }

    public void initializeWorkspacesAndMemberRoles() {
        String sql = "DELETE FROM workspaces WHERE company_id = ?";
        jdbcTemplate.update(sql, companyId);

        sql = "INSERT INTO workspaces (id, created_at, updated_at, company_id, name, description, is_deleted)" +
            " VALUES (?, ?, ?, ?, ?, ?, ?)";

        // Create 2 workspaces: workspace1Id, workspace2Id
        jdbcTemplate.update(sql, workspace1Id, Timestamp.from(Instant.now()), Timestamp.from(Instant.now()), companyId, "Workspace 1", "Workspace 1 Description", false);
        jdbcTemplate.update(sql, workspace2Id, Timestamp.from(Instant.now()), Timestamp.from(Instant.now()), companyId, "Workspace 2", "Workspace 2 Description", false);

        sql = "DELETE FROM workspaces_members_roles WHERE workspace_id IN (?, ?)";
        jdbcTemplate.update(sql, workspace1Id, workspace2Id);

        // Workspaces Members Roles
        sql = "INSERT INTO workspaces_members_roles (id, workspace_id, user_id, role_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";

        //member1Id, member2Id, workspaceManager1Id belong to workspace1Id
        jdbcTemplate.update(sql, wmr_wm_1Id, workspace1Id, workspaceManager1Id, workspaceManagerRoleId, Timestamp.from(Instant.now()), Timestamp.from(Instant.now()));
        jdbcTemplate.update(sql, wmr_m1_1Id, workspace1Id, member1Id, memberRoleId, Timestamp.from(Instant.now()), Timestamp.from(Instant.now()));
        jdbcTemplate.update(sql, wmr_m2_1Id, workspace1Id, member2Id, memberRoleId, Timestamp.from(Instant.now()), Timestamp.from(Instant.now()));

        //member3Id, member4Id, workspaceManager2Id belong to workspace2Id
        jdbcTemplate.update(sql, wmr_wm_2Id, workspace2Id, workspaceManager2Id, workspaceManagerRoleId, Timestamp.from(Instant.now()), Timestamp.from(Instant.now()));
        jdbcTemplate.update(sql, wmr_m1_2Id, workspace2Id, member3Id, memberRoleId, Timestamp.from(Instant.now()), Timestamp.from(Instant.now()));
        jdbcTemplate.update(sql, wmr_m2_2Id, workspace2Id, member4Id, memberRoleId, Timestamp.from(Instant.now()), Timestamp.from(Instant.now()));
    }

    public void initializeProjectsAndMembers() {
        String sql = "DELETE FROM projects WHERE workspace_id IN (?, ?)";
        jdbcTemplate.update(sql, workspace1Id, workspace2Id);

        sql = "INSERT INTO projects (id, created_at, updated_at, description, end_date, is_deleted, leader_id, name, start_date, workspace_id, status, priority)" +
            " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, project1Id, Timestamp.from(Instant.now()), Timestamp.from(Instant.now()), "Project 1 Description", Timestamp.from(Instant.now()), false, member1Id, "Project 1", Timestamp.from(Instant.now()), workspace1Id, "TODO", 0);
        jdbcTemplate.update(sql, project2Id, Timestamp.from(Instant.now()), Timestamp.from(Instant.now()), "Project 2 Description", Timestamp.from(Instant.now()), false, member3Id, "Project 2", Timestamp.from(Instant.now()), workspace2Id, "TODO", 0);

        sql = "DELETE FROM project_members WHERE project_id IN (?, ?)";
        jdbcTemplate.update(sql, project1Id, project2Id);

        sql = "INSERT INTO project_members (id, created_at, updated_at, project_id, subscribed, user_id)" +
            " VALUES (?, ?, ?, ?, ?, ?)";
        //member1Id, member2Id, workspaceManager1Id belong to project1Id
        jdbcTemplate.update(sql, UUID.randomUUID(), Timestamp.from(Instant.now()), Timestamp.from(Instant.now()), project1Id, true, member1Id);
        jdbcTemplate.update(sql, UUID.randomUUID(), Timestamp.from(Instant.now()), Timestamp.from(Instant.now()), project1Id, true, member2Id);
        jdbcTemplate.update(sql, UUID.randomUUID(), Timestamp.from(Instant.now()), Timestamp.from(Instant.now()), project1Id, true, workspaceManager1Id);

        //member3Id, member4Id, workspaceManager2Id belong to project2Id
        jdbcTemplate.update(sql, UUID.randomUUID(), Timestamp.from(Instant.now()), Timestamp.from(Instant.now()), project2Id, true, member3Id);
        jdbcTemplate.update(sql, UUID.randomUUID(), Timestamp.from(Instant.now()), Timestamp.from(Instant.now()), project2Id, true, member4Id);
        jdbcTemplate.update(sql, UUID.randomUUID(), Timestamp.from(Instant.now()), Timestamp.from(Instant.now()), project2Id, true, workspaceManager2Id);
    }
}
