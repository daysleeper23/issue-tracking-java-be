package org.projectmanagement.test_data_factories;

import org.projectmanagement.domain.entities.ProjectMembers;
import org.projectmanagement.domain.repository.ProjectMembersRepoJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProjectMembersDataFactory {
    @Autowired
    ProjectMembersRepoJpa projectMembersRepoJpa;

    public void deleteAll() {
        projectMembersRepoJpa.deleteAll();
    }

    public UUID addMemberToProject(UUID projectId, UUID userId) {
        ProjectMembers projectMember = projectMembersRepoJpa.save(ProjectMembers.builder()
                        .projectId(projectId)
                        .userId(userId)
                        .subscribed(false)
                .build());

        return projectMember.getId();
    }
}
