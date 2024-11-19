package org.projectmanagement.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "roles")
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

    public Roles(UUID id, String name, UUID companyId, Boolean isDeleted, Boolean isSystemRole, Instant createdAt, Instant updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.name = name;
        this.companyId = companyId;
        this.isDeleted = isDeleted;
        this.isSystemRole = isSystemRole;
    }
}
