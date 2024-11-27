package org.projectmanagement.application.dto.invitations;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import net.bytebuddy.agent.Installer;
import org.hibernate.validator.constraints.UUID;

import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = false)
public record InvitationsCreate(
        @Email
        String userEmail,
        @UUID
        String workspaceId,
        @UUID
        String roleId,
        @NotNull
        Instant expiredAt,
        boolean isAdmin
) {
}
