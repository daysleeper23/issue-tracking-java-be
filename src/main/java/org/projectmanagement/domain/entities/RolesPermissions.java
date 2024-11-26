package org.projectmanagement.domain.entities;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "roles_permissions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"role_id", "permission_id"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RolesPermissions {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @NotNull
    @Column(name = "role_id", nullable = false)
    private UUID roleId;

    @NotNull
    @Column(name = "permission_id", nullable = false)
    private UUID permissionId;

}
