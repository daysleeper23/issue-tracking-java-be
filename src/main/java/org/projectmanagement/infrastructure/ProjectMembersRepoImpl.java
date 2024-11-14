package org.projectmanagement.infrastructure;

import org.projectmanagement.domain.entities.ProjectMembers;
import org.projectmanagement.domain.repository.ProjectMembersRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ProjectMembersRepoImpl implements ProjectMembersRepository {
    private final InMemoryDatabase inMemoryDatabase;

    ProjectMembersRepoImpl(InMemoryDatabase inMemoryDatabase) {
        this.inMemoryDatabase = inMemoryDatabase;
    }

    @Override
    public ProjectMembers save(ProjectMembers projectMember) {
        return inMemoryDatabase.saveProjectMember(projectMember);
    }

    @Override
    public Optional<ProjectMembers> findOneById(UUID id) {
        return inMemoryDatabase.projectMembers.stream().filter(projectMember -> projectMember.getId().equals(id)).findFirst();
    }

    @Override
    public List<ProjectMembers> findAllProjectMembersByProjectId(UUID projectId) {
        return inMemoryDatabase.projectMembers.stream().filter(projectMember -> projectMember.getProjectId().equals(projectId)).toList();
    }

    @Override
    public List<ProjectMembers> findAllProjectsMemberIsPartOfByUserId(UUID userId){
        return inMemoryDatabase.projectMembers.stream().filter(projectMember -> projectMember.getUserId().equals(userId)).toList();
    }

    @Override
    public void deleteProjectMemberById(UUID id) {
        inMemoryDatabase.projectMembers.removeIf(projectMember -> projectMember.getId().equals(id));
    }

    @Override
    public void deleteProjectMemberByProjectIdAndUserId(UUID projectId, UUID userId) {
        inMemoryDatabase.projectMembers.removeIf(projectMember -> projectMember.getProjectId().equals(projectId) && projectMember.getUserId().equals(userId));
    }
}
