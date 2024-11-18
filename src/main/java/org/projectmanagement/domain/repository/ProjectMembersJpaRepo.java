package org.projectmanagement.domain.repository;

import org.projectmanagement.domain.entities.ProjectMembers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectMembersJpaRepo extends JpaRepository<ProjectMembers, UUID> {
    List<ProjectMembers> findAllByProjectId(UUID projectId);
    List<ProjectMembers> findAllByUserId(UUID userId);
    void deleteByProjectIdAndUserId(UUID projectId, UUID userId);
}
