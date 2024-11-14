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

    @Getter
    public final List<Tasks> tasks;
    @Getter
    public final List<TaskSubscribers> taskSubscribers;
    UUID companyId = UUID.fromString("b541ade4-9cfa-4664-b9e3-d9923ae02fb4");
    UUID roleAdminId = UUID.fromString("7b149139-6b39-4e5c-9e24-70c092df4a5d");
    UUID roleDeveloperId = UUID.fromString("78b4fe40-5f93-40ef-9095-7b25c7bb62ff");
    UUID workspaceId1 = UUID.fromString("6892ddd0-8a88-4aac-a562-1e1656732f9f");
    UUID workspaceId2 = UUID.fromString("f7e6c463-7930-446c-b871-59db53cf5c01");
    UUID user1 = UUID.fromString("03093311-73ce-4264-99c9-886caa7bd1e1");
    UUID user2 = UUID.fromString("6ea39d08-9f8d-4271-ad98-be7eddd4c7fc");
    UUID user3 = UUID.fromString("c15fa709-67aa-4b8a-b82d-dac8f306e673"); // company manager
    UUID project1 = UUID.fromString("6aff4b4a-55e1-41ed-9b9d-56927b2f3f7b");
    UUID project2 = UUID.fromString("95034052-1401-41f7-adb4-bcb1b9c23daf");
    UUID projectMember = UUID.fromString("216967d1-9f2d-4f53-8e8a-12c269a9c535");
    UUID companyManager = UUID.fromString("96cdb8ba-7304-4ae1-9be5-b6a8551bc64c");


    public InMemoryDatabase() {

        System.out.println("ID: " + UUID.randomUUID());
        users = new ArrayList<>(
                List.of(
                        new Users(user1,
                                "User 1",
                                "u1@test.com",
                                "password",
                                "Developer",
                                true,
                                companyId,
                                true,
                                false,
                                Instant.now(),
                                Instant.now()
                        ),
                        new Users(user2,
                                "User 2",
                                "u2@test.com",
                                "password",
                                "Developer",
                                true,
                                companyId,
                                false,
                                false,
                                Instant.now(),
                                Instant.now()
                        ),
                        new Users(user3,
                                "User 3",
                                "u3@test.com",
                                "password",
                                "CTO",
                                true,
                                companyId,
                                false,
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
                                false,
                                Instant.now(),
                                Instant.now()
                        ),
                        new Workspaces(workspaceId2,
                                "Workspace B",
                                "Description B",
                                companyId,
                                false,
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
                                false,
                                Instant.now(),
                                Instant.now()
                        ),
                        new Roles(roleDeveloperId,
                                "Developer",
                                companyId,
                                false,
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

        projects = new ArrayList<>(
                List.of(
                        Projects.builder()
                                .id(project1)
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
                                .id(project2)
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

        companyManagers = new ArrayList<>(
                List.of(
                        CompanyManagers.builder()
                                .id(UUID.randomUUID())
                                .companyId(companyId)
                                .userId(user3)
                                .roleId(roles.get(0).getId())
                                .createdAt(Instant.now())
                                .updatedAt(Instant.now())
                                .build()
                )
        );

        roles = new ArrayList<>();

        tasks = new ArrayList<>();

        taskSubscribers = new ArrayList<>();
    /*
     *
     * COMPANIES
     *
     */
    public Companies saveCompany(Companies company) {
        companies.add(company);
        return company;
    }

    /*
     *
     * USERS
     *
     */
    public Users saveUser(Users user) {
        users.add(user);
        return user;
    }

    public void deleteUserById(UUID id) {
        Users user = users.
                stream().filter(u -> u.getId().equals(id) && !u.getIsDeleted()).
                findFirst().orElse(null);

        if (user == null) {
            return;
        }

        //update isDeleted to true
        users.removeIf(u -> u.getId().equals(id));
        user.setIsDeleted(true);
        users.add(user);
    }

    public List<Users> getUsersByCompany(UUID companyId) {
        return users.stream().filter(u -> u.getCompanyId().equals(companyId) && !u.getIsDeleted()).toList();
    }

    /*
     *
     * ROLES
     *
     */
    public Roles saveRole(Roles role) {
        roles.add(role);
        return role;
    }

    public Roles getRoleByName(String name, UUID companyId) {
        return roles.stream()
                .filter(r -> r.getName().equals(name) && r.getCompanyId().equals(companyId) && !r.getIsDeleted())
                .findFirst().orElse(null);
    }

    public Roles getRoleById(UUID id) {
        return roles.stream().filter(r -> r.getId().equals(id) && !r.getIsDeleted()).findFirst().orElse(null);
    }

    public List<Roles> getRolesByCompanyId(UUID companyId) {
        return roles.stream().filter(r -> r.getCompanyId().equals(companyId) && !r.getIsDeleted()).toList();
    }

    public void deleteRoleById(UUID id) {
        Roles role = roles.stream().filter(r -> r.getId().equals(id)).findFirst().orElse(null);
        if (role == null) {
            return;
        }

        //update isDeleted to true
        roles.removeIf(r -> r.getId().equals(id));
        role.setIsDeleted(true);
        roles.add(role);
    }

    /*
     *
     * WORKSPACES MEMBERS ROLES
     *
     */
    public WorkspacesMembersRoles saveWmr(WorkspacesMembersRoles wmr) {
        wmrs.add(wmr);
        return wmr;
    }

    /*
     *
     * WORKSPACES
     *
     */
    public Workspaces saveWorkspace(Workspaces workspace) {
        workspaces.add(workspace);
        return workspace;
    }

    public void deleteWorkspaceById(UUID id) {
        Workspaces workspace = workspaces.stream().filter(w -> w.getId().equals(id) && !w.getIsDeleted()).findFirst().orElse(null);
        if (workspace == null) {
            return;
        }

        //update isDeleted to true
        workspaces.removeIf(w -> w.getId().equals(id));
        workspace.setIsDeleted(true);
        workspaces.add(workspace);
    }

    public List<Workspaces> getActiveWorkspacesByCompany(UUID companyId) {
        return workspaces.stream().filter(w -> !w.getIsDeleted() && w.getCompanyId().equals(companyId)).toList();
    }

    public Workspaces getActiveWorkspaceById(UUID id) {
        return workspaces.stream().filter(w -> w.getId().equals(id) && !w.getIsDeleted()).findFirst().orElse(null);
    }

    public Workspaces getWorkspaceByIdAndUpdate(UUID id, Workspaces workspace) {
        workspaces.removeIf(w -> w.getId().equals(id));
        workspaces.add(workspace);
        return workspace;
    }

    /*
     *
     * Projects
     *
     */

    public Projects saveProject(Projects project) {
        projects.add(project);
        return project;
    }

    /*
     *
     * CompanyManagers
     *
     */

    public CompanyManagers saveCompanyManager(CompanyManagers companyManager) {
        companyManagers.add(companyManager);
        return companyManager;
    }

    /*
     *
     * ProjectMembers
     *
     */

    public ProjectMembers saveProjectMember(ProjectMembers projectMember) {
        projectMembers.add(projectMember);
        return projectMember;
    }

}