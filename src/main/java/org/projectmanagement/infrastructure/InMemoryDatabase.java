package org.projectmanagement.infrastructure;

import lombok.Getter;
import org.projectmanagement.domain.entities.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Getter
public class InMemoryDatabase {
    private static final InMemoryDatabase INSTANCE = new InMemoryDatabase();

    public static InMemoryDatabase getInstance() {
        return INSTANCE;
    }

    public List<Users> users;
    public List<Workspaces> workspaces;
    public List<Roles> roles;
    public List<Companies> companies;
    public List<WorkspacesMembersRoles> wmrs;

    UUID companyId = UUID.fromString("b541ade4-9cfa-4664-b9e3-d9923ae02fb4");
    UUID roleAdminId = UUID.fromString("7b149139-6b39-4e5c-9e24-70c092df4a5d");
    UUID roleDeveloperId = UUID.fromString("78b4fe40-5f93-40ef-9095-7b25c7bb62ff");
    UUID workspaceId1 = UUID.fromString("6892ddd0-8a88-4aac-a562-1e1656732f9f");
    UUID workspaceId2 = UUID.fromString("f7e6c463-7930-446c-b871-59db53cf5c01");

    public InMemoryDatabase() {

        System.out.println("ID: " + UUID.randomUUID());
        users = new ArrayList<>(
                List.of(
                        new Users(UUID.randomUUID(),
                                "User 1",
                                "u1@test.com",
                                "password",
                                "Developer",
                                true,
                                companyId,
                                true,
                                Instant.now(),
                                Instant.now()
                        ),
                        new Users(UUID.randomUUID(),
                                "User 2",
                                "u2@test.com",
                                "password",
                                "Developer",
                                true,
                                companyId,
                                false,
                                Instant.now(),
                                Instant.now()
                        )
                )
        );

        workspaces = new ArrayList<>(
                List.of(
                        new Workspaces(workspaceId1,
                                "Workspace A",
                                "Description A",
                                companyId,
                                Instant.now(),
                                Instant.now()
                        ),
                        new Workspaces(workspaceId2,
                                "Workspace B",
                                "Description B",
                                companyId,
                                Instant.now(),
                                Instant.now()
                        )
                )
        );

        roles = new ArrayList<>(
                List.of(
                        new Roles(roleAdminId,
                                "Admin",
                                companyId,
                                Instant.now(),
                                Instant.now()
                        ),
                        new Roles(roleDeveloperId,
                                "Developer",
                                companyId,
                                Instant.now(),
                                Instant.now()
                        )
                )
        );
        companies = new ArrayList<>(
                List.of(
                        new Companies(UUID.fromString("b541ade4-9cfa-4664-b9e3-d9923ae02fb4"),
                                "Company 1",
                                "Description 1",
                                UUID.randomUUID(),
                                Instant.now(),
                                Instant.now()
                        )
                )
        );

        wmrs = new ArrayList<>(
                List.of(
                        new WorkspacesMembersRoles(UUID.randomUUID(),
                                workspaceId1,
                                users.get(0).getId(),
                                roles.get(0).getId(),
                                Instant.now(),
                                Instant.now()
                        ),
                        new WorkspacesMembersRoles(UUID.randomUUID(),
                                workspaceId1,
                                users.get(1).getId(),
                                roles.get(1).getId(),
                                Instant.now(),
                                Instant.now()
                        ),
                        new WorkspacesMembersRoles(UUID.randomUUID(),
                                workspaceId2,
                                users.get(0).getId(),
                                roles.get(1).getId(),
                                Instant.now(),
                                Instant.now()
                        ),
                        new WorkspacesMembersRoles(UUID.randomUUID(),
                                workspaceId2,
                                users.get(1).getId(),
                                roles.get(0).getId(),
                                Instant.now(),
                                Instant.now()
                        )

                )
        );
    }

    public Companies saveCompany(Companies company) {
        companies.add(company);
        return company;
    }

    public Users saveUser(Users user) {
        users.add(user);
        return user;
    }

    public Roles saveRole(Roles role) {
        roles.add(role);
        return role;
    }

    public WorkspacesMembersRoles saveWmr(WorkspacesMembersRoles wmr) {
        wmrs.add(wmr);
        return wmr;
    }
}