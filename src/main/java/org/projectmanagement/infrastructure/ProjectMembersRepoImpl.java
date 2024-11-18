package org.projectmanagement.infrastructure;

import org.projectmanagement.domain.entities.ProjectMembers;
import org.projectmanagement.domain.repository.ProjectMembersJpaRepo;
import org.projectmanagement.domain.repository.ProjectMembersRepository;
import org.projectmanagement.domain.repository.ProjectsJpaRepo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ProjectMembersRepoImpl implements ProjectMembersRepository {
    private final ProjectMembersJpaRepo projectMembersJpaRepo;

    ProjectMembersRepoImpl(ProjectMembersJpaRepo projectMembersJpaRepo) {
        this.projectMembersJpaRepo = projectMembersJpaRepo;
    }

    @Override
    public ProjectMembers save(ProjectMembers projectMember) {
        return projectMembersJpaRepo.save(projectMember);
    }

    @Override
    public Optional<ProjectMembers> findOneById(UUID id) {
        return projectMembersJpaRepo.findById(id);
    }

    @Override
    public List<ProjectMembers> findAllProjectMembersByProjectId(UUID projectId) {
        return projectMembersJpaRepo.findAllByProjectId(projectId);
    }

    @Override
    public List<ProjectMembers> findAllProjectsMemberIsPartOfByUserId(UUID userId){
        return projectMembersJpaRepo.findAllByUserId(userId);
    }

    @Override
    public void deleteProjectMemberById(UUID id) {
        projectMembersJpaRepo.deleteById(id);
    }

    @Override
    public void deleteProjectMemberByProjectIdAndUserId(UUID projectId, UUID userId) {
        projectMembersJpaRepo.deleteByProjectIdAndUserId(projectId, userId);
    }
}
