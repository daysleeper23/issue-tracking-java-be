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
@Table(name = "workspaces", uniqueConstraints = {
        @UniqueConstraint(name = "uc_workspace_name",columnNames = {"name", "company_id"})
})
public class Workspaces extends BaseEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private UUID companyId;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean isDeleted;
    //Todo: Lombok does not support the creation with super class constructor
    // so either remove the constructor or keep it if you are using it for testing
    public Workspaces(UUID id, String name, String description, UUID companyId, Boolean isDeleted, Instant createdAt, Instant updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.name = name;
        this.description = description;
        this.companyId = companyId;
        this.isDeleted = isDeleted;
    }
}
