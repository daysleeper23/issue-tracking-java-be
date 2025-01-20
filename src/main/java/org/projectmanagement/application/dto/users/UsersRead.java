package org.projectmanagement.application.dto.users;

import java.time.Instant;
import java.util.UUID;

public record UsersRead (
    UUID id,
    String name,
    String email,
    String passwordHash,
    String title,
    Boolean isActive,
    UUID companyId,
    Boolean isOwner,
    String avatarUrl,
    Instant updatedAt,
    Instant createdAt
) {
}