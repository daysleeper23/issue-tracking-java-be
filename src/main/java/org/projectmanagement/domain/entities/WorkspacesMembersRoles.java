package org.projectmanagement.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.util.UUID;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "workspaces_members_roles")
public class WorkspacesMembersRoles extends BaseEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(nullable = false)
    private UUID workspaceId;

    @Column(nullable = false)
    private UUID userId;

    @Setter
    @Column(nullable = false)
    private UUID roleId;

    public WorkspacesMembersRoles(UUID id, UUID workspaceId, UUID userId, UUID roleId, Instant createdAt, Instant updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.workspaceId = workspaceId;
        this.userId = userId;
        this.roleId = roleId;
    }

}
