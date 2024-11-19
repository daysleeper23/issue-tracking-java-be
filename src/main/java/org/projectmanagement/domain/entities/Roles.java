package org.projectmanagement.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NonNull
@AllArgsConstructor
@Builder
@Entity
public class Roles {
    @Id
    @GeneratedValue(generator = "UUID")
    private final UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private UUID companyId;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean isDeleted;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean isSystemRole;

    @Column(nullable = false, columnDefinition = "timestamp with time zone default now()")
    private Instant createdAt;

    @Column(nullable = false, columnDefinition = "timestamp with time zone default now()")
    private Instant updatedAt;
}
