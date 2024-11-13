package org.projectmanagement.infrastructure;

import lombok.Getter;
import org.projectmanagement.domain.entities.Companies;
import org.projectmanagement.domain.entities.Roles;
import org.projectmanagement.domain.entities.Users;
import org.projectmanagement.domain.entities.Workspaces;
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

    UUID companyId = UUID.fromString("b541ade4-9cfa-4664-b9e3-d9923ae02fb4");
    UUID roleAdminId = UUID.fromString("7b149139-6b39-4e5c-9e24-70c092df4a5d");
    UUID roleDeveloperId = UUID.fromString("78b4fe40-5f93-40ef-9095-7b25c7bb62ff");
    UUID workspaceId = UUID.randomUUID();

//    System.out.println("C: " + companyId);
//            ("W: " + workspaceId);

    public InMemoryDatabase() {

        System.out.println("W ID: " + UUID.randomUUID());
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
                        new Workspaces(UUID.randomUUID(),
                                "Workspace A",
                                "Description A",
                                companyId,
                                Instant.now(),
                                Instant.now()
                        ),
                        new Workspaces(UUID.randomUUID(),
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
}