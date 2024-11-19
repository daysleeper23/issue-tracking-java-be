package org.projectmanagement.domain.repository;

import org.projectmanagement.domain.entities.ProjectMembers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectMembersJpaRepo extends JpaRepository<ProjectMembers, UUID> {
    @Query(value = "SELECT * " +
            "FROM project_members pm " +
            "WHERE pm.project_id = :projectId ",
            nativeQuery = true)
    List<ProjectMembers> findAllByProjectId(@Param("projectId") UUID projectId);

    @Query(value = "SELECT * " +
            "FROM project_members pm " +
            "WHERE pm.user_id = :userId ",
            nativeQuery = true)
    List<ProjectMembers> findAllByUserId(@Param("userId") UUID userId);


    @Query(value = "DELETE " +
            "FROM project_members pm " +
            "WHERE pm.project_id = :projectId " +
            "AND pm.user_id = :userId",
            nativeQuery = true)
    void deleteByProjectIdAndUserId(@Param("projectId")UUID projectId, @Param("userId") UUID userId);
}
