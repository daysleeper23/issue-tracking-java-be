package org.projectmanagement.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.util.UUID;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "workspaces_members_roles")
public class WorkspacesMembersRoles {

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

    @Column(nullable = false, columnDefinition = "timestamp with time zone default now()")
    private Instant createdAt;

    @Column(nullable = false, columnDefinition = "timestamp with time zone default now()")
    private Instant updatedAt;
}
