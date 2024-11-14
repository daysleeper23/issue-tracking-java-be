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
    public List<Projects> projects;
    public List<CompanyManagers> companyManagers;
    public List<ProjectMembers> projectMembers;

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