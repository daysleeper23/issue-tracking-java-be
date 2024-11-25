package org.projectmanagement.domain.entities;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.projectmanagement.domain.exceptions.InvalidInputException;


import java.time.Instant;
import java.util.UUID;

@Getter
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "invitations")
public class Invitations extends BaseEntity {

    @Setter(AccessLevel.NONE)
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

    @Column(name = "is_admin")
    private boolean isAdmin;

    @Column(name = "is_del")
    private boolean isDel;

}
