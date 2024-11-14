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
    Instant updatedAt,
    Instant createdAt
) {
}