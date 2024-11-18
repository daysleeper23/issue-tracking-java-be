package org.projectmanagement.domain.entities;
import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@EntityListeners(AuditingEntityListener.class)
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

//  @CreatedDate
    private Instant createdAt;

//  @LastModifiedDate
    private Instant updatedAt;
}