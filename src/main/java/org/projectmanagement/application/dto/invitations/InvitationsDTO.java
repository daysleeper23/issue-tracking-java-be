package org.projectmanagement.application.dto.invitations;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;

import java.time.Instant;

public record InvitationsDTO(
        @Email
        String userEmail,
        @UUID
        String workspaceId,
        @UUID
        String roleId,
        @NotNull
        Instant expiredAt
) {
}
