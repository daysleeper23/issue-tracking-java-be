package org.projectmanagement.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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

//    @CreatedDate
    private Instant createdAt;

//    @LastModifiedDate
    private Instant updatedAt;
}
