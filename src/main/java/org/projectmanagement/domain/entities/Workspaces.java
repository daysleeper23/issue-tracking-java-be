package org.projectmanagement.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "workspaces")
public class Workspaces {
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

    @Column(nullable = false, columnDefinition = "timestamp with time zone default now()")
    private Instant createdAt;

    @Column(nullable = false, columnDefinition = "timestamp with time zone default now()")
    private Instant updatedAt;
}
