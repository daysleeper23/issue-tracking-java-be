package org.projectmanagement.infrastructure;

import org.projectmanagement.domain.entities.ProjectMembers;
import org.projectmanagement.domain.repository.ProjectMembersRepoJpa;
import org.projectmanagement.domain.repository.ProjectMembersRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ProjectMembersRepoImpl implements ProjectMembersRepository {
    private final ProjectMembersRepoJpa projectMembersRepoJpa;

    ProjectMembersRepoImpl(ProjectMembersRepoJpa projectMembersRepoJpa) {
        this.projectMembersRepoJpa = projectMembersRepoJpa;
    }

    @Override
    public ProjectMembers save(ProjectMembers projectMember) {
        return projectMembersRepoJpa.save(projectMember);
    }

    @Override
    public Optional<ProjectMembers> findOneById(UUID id) {
        return projectMembersRepoJpa.findById(id);
    }

    @Override
    public List<ProjectMembers> findAllProjectMembersByProjectId(UUID projectId) {
        return projectMembersRepoJpa.findAllByProjectId(projectId);
    }

    @Override
    public List<ProjectMembers> findAllProjectsMemberIsPartOfByUserId(UUID userId){
        return projectMembersRepoJpa.findAllByUserId(userId);
    }

    @Override
    public void deleteProjectMemberById(UUID id) {
        projectMembersRepoJpa.deleteById(id);
    }

    @Override
    public void deleteProjectMemberByProjectIdAndUserId(UUID projectId, UUID userId) {
        projectMembersRepoJpa.deleteByProjectIdAndUserId(projectId, userId);
    }

    @Override
    public Optional<ProjectMembers> findByProjectIdAndUserId(UUID projectId, UUID userId) {
        return projectMembersRepoJpa.findByProjectIdAndUserId(projectId, userId);
    }
}
