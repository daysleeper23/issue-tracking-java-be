package org.projectmanagement.application.dto.invitations;


import java.time.Instant;

public record InvitationsInfo(String id,
                              String userEmail,
                              String workspaceId,
                              String companyId,
                              String roleId,
                              String invitedBy,
                              Instant expiredAt,
                              Boolean isAdmin,
                              Instant createdAt,
                              Instant updatedAt
) {
}
