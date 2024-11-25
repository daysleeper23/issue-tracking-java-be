package org.projectmanagement.test_data_factories;

import org.projectmanagement.domain.entities.Projects;
import org.projectmanagement.domain.enums.DefaultStatus;
import org.projectmanagement.domain.repository.ProjectsRepoJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class ProjectsDataFactory {
    @Autowired
    private ProjectsRepoJpa projectsRepoJpa;

    public void deleteAll() {
        projectsRepoJpa.deleteAll();
    }

    public UUID createProject(String projectName, UUID workspaceId, UUID leaderId) {
        Projects project = projectsRepoJpa.save(Projects.builder()
                .name(projectName)
                .description("A test project")
                .status(DefaultStatus.TODO)
                .priority(1)
                .workspaceId(workspaceId)
                .leaderId(leaderId)
                .isDeleted(false)
                .startDate(Instant.now())
                .endDate(Instant.now().plusSeconds(3600))
                .build());

        return project.getId();
    }
}
