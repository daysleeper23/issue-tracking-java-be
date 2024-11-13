package org.projectmanagement.application.dto.users;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class UsersRead {
    private UUID id;
    private String name;
    private String email;
    private String passwordHash;
    private String title;
    private Boolean isActive;
    private UUID companyId;
    private Boolean isOwner;
    private Instant updatedAt;
}
