package org.projectmanagement.domain.entities;
import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "users")
public class Users extends BaseEntity{

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

//    @Column(nullable = false, columnDefinition = "timestamp with time zone default now()")
//    private Instant createdAt;
//
//    @Column(nullable = false, columnDefinition = "timestamp with time zone default now()")
//    private Instant updatedAt;
    //Todo: Remove the constructor after you have implemented jpa
    public Users(UUID id, String name, String email, String passwordHash, String title, Boolean isActive, UUID companyId, Boolean isOwner, Boolean isDeleted, Instant createdAt, Instant updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.title = title;
        this.isActive = isActive;
        this.companyId = companyId;
        this.isOwner = isOwner;
        this.isDeleted = isDeleted;
    }
}