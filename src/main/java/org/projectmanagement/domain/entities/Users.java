package org.projectmanagement.domain.entities;
import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    private String title;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private Boolean isActive;

    private UUID companyId;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean isOwner;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean isDeleted;

    @Column(nullable = false, columnDefinition = "timestamp with time zone default now()")
    private Instant createdAt;

    @Column(nullable = false, columnDefinition = "timestamp with time zone default now()")
    private Instant updatedAt;
}