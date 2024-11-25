package org.projectmanagement.domain.repository;

import org.projectmanagement.domain.entities.ProjectMembers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface ProjectMembersRepository {

    ProjectMembers save(ProjectMembers projectMember);

    Optional<ProjectMembers> findOneById(UUID id);

    List<ProjectMembers> findAllProjectMembersByProjectId(UUID projectId);

    List<ProjectMembers> findAllProjectsMemberIsPartOfByUserId(UUID userId);

    void deleteProjectMemberById(UUID id);

    void deleteProjectMemberByProjectIdAndUserId(UUID projectId, UUID userId);

    Optional<ProjectMembers> findByProjectIdAndUserId(UUID projectId, UUID userId);
}
