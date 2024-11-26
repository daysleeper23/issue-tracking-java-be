package org.projectmanagement.domain.entities;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.projectmanagement.domain.exceptions.InvalidInputException;


import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@Getter
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "invitations", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_email"})
})
public class Invitations extends BaseEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Email
    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "company_id")
    private UUID companyId;

    @Column(name = "workspace_id")
    private UUID workspaceId;

    @Column(name = "role_id")
    private UUID roleId;

    @Column(name = "invited_by")
    private UUID invitedBy;

    @Column(name = "expired_at")
    private Instant expiredAt;

    @Column(nullable = false,name = "is_admin",columnDefinition = "boolean default false")
    private boolean isAdmin;

    @Column(nullable = false,name = "is_del", columnDefinition = "boolean default false")
    private boolean isDel = false;

}
