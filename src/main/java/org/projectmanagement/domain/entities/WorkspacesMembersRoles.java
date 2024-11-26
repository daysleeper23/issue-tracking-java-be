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
@Table(name = "workspaces_members_roles", uniqueConstraints = {
        @UniqueConstraint(name="uc_workspace_member_role",columnNames = {"workspace_Id", "user_Id", "role_Id"})
})
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
}
