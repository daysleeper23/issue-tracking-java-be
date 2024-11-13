package org.projectmanagement.infrastructure;

import lombok.Getter;
import org.projectmanagement.domain.entities.Roles;
import org.projectmanagement.domain.entities.Users;
import org.projectmanagement.domain.entities.Workspaces;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class InMemoryDatabase {
    @Getter
    public List<Users> users;
    public List<Workspaces> workspaces;
    public List<Roles> roles;


    public InMemoryDatabase() {
        UUID companyId = UUID.randomUUID();
        System.out.println("Company ID: " + companyId);
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

        roles = new ArrayList<>();
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