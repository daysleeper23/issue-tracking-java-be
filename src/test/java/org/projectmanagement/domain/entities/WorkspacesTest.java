package org.projectmanagement.domain.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.Mockito.when;

public class WorkspacesTest {
    Workspaces workspaces1;
    UUID compId1;
    Instant creationTime1;

    @BeforeEach
    public void setup() {
        workspaces1 = Mockito.mock(Workspaces.class);
        compId1 = UUID.randomUUID();
        creationTime1 = Instant.now();

        when(workspaces1.getName()).thenReturn("Workspace 1");
        when(workspaces1.getDescription()).thenReturn("Workspace 1 description");
        when(workspaces1.getCompanyId()).thenReturn(compId1);
        when(workspaces1.getCreatedAt()).thenReturn(creationTime1);
        when(workspaces1.getUpdatedAt()).thenReturn(creationTime1);
    }

    @Test
    public void testGetter() {
        assert workspaces1.getName().equals("Workspace 1");
        assert workspaces1.getDescription().equals("Workspace 1 description");
        assert workspaces1.getCompanyId().equals(compId1);
        assert workspaces1.getCreatedAt().equals(creationTime1);
        assert workspaces1.getUpdatedAt().equals(creationTime1);
    }
}
