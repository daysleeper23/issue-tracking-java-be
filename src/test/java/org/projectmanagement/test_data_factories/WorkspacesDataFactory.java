package org.projectmanagement.test_data_factories;


import org.projectmanagement.domain.entities.Workspaces;
import org.projectmanagement.domain.repository.WorkspacesRepoJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class WorkspacesDataFactory {
    @Autowired
    private WorkspacesRepoJpa workspacesRepoJpa;

    public void deleteAll() {
        workspacesRepoJpa.deleteAll();
    }

    public UUID createWorkspace(UUID companyId) {
        Workspaces workspace = workspacesRepoJpa.save(Workspaces.builder()
                .name("Test Workspace")
                .description("A test workspace")
                .companyId(companyId)
                .isDeleted(false)
                .build());

        return workspace.getId();
    }

}
