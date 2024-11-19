package org.projectmanagement.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "company_managers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CompanyManagers {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @NonNull
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @NonNull
    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @NonNull
    @Column(name = "role_id", nullable = false)
    private UUID roleId;

    @Column(name = "created_at")
    private  Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

}
