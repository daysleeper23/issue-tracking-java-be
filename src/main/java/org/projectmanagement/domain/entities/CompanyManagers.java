package org.projectmanagement.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "company_managers", uniqueConstraints = {
        @UniqueConstraint(name="uc_cm_member_role",columnNames = {"user_id", "company_id", "role_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
public class CompanyManagers extends BaseEntity{
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

}
