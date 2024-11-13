package org.projectmanagement.application.dto.users;

import lombok.*;

import java.util.UUID;

@NonNull
@Getter
@Setter
@AllArgsConstructor
public class UsersCreate {
    private String name;

    private String email;

    private String passwordHash;

    private String title;

    private Boolean isActive;

    private UUID companyId;

    private Boolean isOwner;
}
