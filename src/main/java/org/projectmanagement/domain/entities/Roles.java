package org.projectmanagement.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "roles", uniqueConstraints = {
        @UniqueConstraint(name="uc_role_name",columnNames = {"name", "company_id"})
})
public class Roles extends BaseEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private UUID companyId;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean isDeleted;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean isSystemRole;

//    @Column(nullable = false, columnDefinition = "timestamp with time zone default now()")
//    private Instant createdAt;
//
//    @Column(nullable = false, columnDefinition = "timestamp with time zone default now()")
//    private Instant updatedAt;
    //Todo: Lombok does not support the creation with super class constructor
    // so either remove the constructor or keep it if you are using it for testing
    public Roles(UUID id, String name, UUID companyId, Boolean isDeleted, Boolean isSystemRole, Instant createdAt, Instant updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.name = name;
        this.companyId = companyId;
        this.isDeleted = isDeleted;
        this.isSystemRole = isSystemRole;
    }

    @Getter
    @RequiredArgsConstructor
    public enum SystemRoles {
        ADMIN("Super Admin"),
        COMPANY_MANAGER("Company Manager"),
        WORKSPACE_MANAGER("Workspace Manager"),
        MEMBER("Member");

        private final String name;

    }
}
