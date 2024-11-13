package org.projectmanagement.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.util.UUID;

@NonNull
@Getter
@AllArgsConstructor
public class WorkspacesMembersRoles {
    @Id
    private final UUID id;

    private final UUID workspaceId;

    private final UUID userId;

    @Setter
    private UUID roleId;

//    @CreatedDate
    private final Instant createdAt;

//    @LastModifiedDate
    private Instant updatedAt;
}
