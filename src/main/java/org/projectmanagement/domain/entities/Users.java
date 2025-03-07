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
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "uc_user_email",columnNames = {"email"})
})
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

    @Column(name = "avatar_url")
    private String avatarUrl;

    //Todo: Lombok does not support the creation with super class constructor
    // so either remove the constructor or keep it if you are using it for testing
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