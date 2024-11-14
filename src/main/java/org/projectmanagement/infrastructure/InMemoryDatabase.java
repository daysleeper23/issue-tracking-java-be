package org.projectmanagement.infrastructure;

import lombok.Getter;
import org.projectmanagement.domain.entities.*;
import org.projectmanagement.domain.enums.DefaultStatus;
import org.projectmanagement.domain.repository.ProjectsRepository;
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
    public List<Projects> projects;
    public List<CompanyManagers> companyManagers;
    public List<ProjectMembers> projectMembers;



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
        System.out.println("Workspace A ID: " + workspaces.get(0).getId());
        roles = new ArrayList<>(
                List.of(
                        new Roles(UUID.randomUUID(),
                                "Company Manager",
                                Instant.now(),
                                Instant.now()
                        )
                )
        );

        projects = new ArrayList<>(
                List.of(
                        Projects.builder()
                                .id(UUID.randomUUID())
                                .name("Project A")
                                .description("Belongs to Workspace A")
                                .startDate(null)
                                .endDate(null)
                                .priority(0)
                                .status(DefaultStatus.TODO)
                                .leaderId(users.get(0).getId())
                                .workspaceId(workspaces.get(0).getId())
                                .createdAt(Instant.now())
                                .updatedAt(Instant.now())
                                .build(),
                        Projects.builder()
                                .id(UUID.randomUUID())
                                .name("Project B")
                                .description("Belongs to Workspace B")
                                .startDate(null)
                                .endDate(null)
                                .priority(2)
                                .status(DefaultStatus.IN_PROGRESS)
                                .leaderId(users.get(1).getId())
                                .workspaceId(workspaces.get(1).getId())
                                .createdAt(Instant.now())
                                .updatedAt(Instant.now())
                                .build()
                        )
                );

        projectMembers = new ArrayList<>(
                List.of(
                        ProjectMembers.builder()
                                .id(UUID.randomUUID())
                                .userId(users.get(1).getId())
                                .projectId(projects.get(0).getId())
                                .subscribed(false)
                                .createdAt(Instant.now())
                                .updatedAt(Instant.now())
                                .build()
                )
        );

        System.out.println("Project A ID: " + projects.get(0).getId());
        System.out.println("User 2 ID: " + users.get(1).getId());

        UUID managerId = UUID.randomUUID();
        users.add(new Users(managerId,
                "User 3",
                "u3@test.com",
                "password",
                "CTO",
                true,
                companyId,
                false,
                Instant.now(),
                Instant.now()
        ));

        companyManagers = new ArrayList<>(
                List.of(
                        CompanyManagers.builder()
                                .id(UUID.randomUUID())
                                .companyId(companyId)
                                .userId(managerId)
                                .roleId(roles.get(0).getId())
                                .createdAt(Instant.now())
                                .updatedAt(Instant.now())
                                .build()
                )
        );

    }

    public Users saveUser(Users user) {
        users.add(user);
        return user;
    }

    public Roles saveRole(Roles role) {
        roles.add(role);
        return role;
    }

    public Projects saveProject(Projects project) {
        projects.add(project);
        return project;
    }

    public CompanyManagers saveCompanyManager(CompanyManagers cm) {
        companyManagers.add(cm);
        return cm;
    }

    public ProjectMembers saveProjectMember(ProjectMembers pm) {
        projectMembers.add(pm);
        return pm;
    }

}