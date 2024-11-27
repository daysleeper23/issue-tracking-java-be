package org.projectmanagement.application.dto.users;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@JsonIgnoreProperties(ignoreUnknown = false)
public record UsersUpdate(
    @Pattern(regexp = "\\S.*", message = "must not be empty or whitespace")
    String name,

    @Pattern(regexp = "\\S.*", message = "must not be empty or whitespace")
    @Email(message = "must be a valid email address")
    String email,

    @Pattern(regexp = "\\S.*", message = "must not be empty or whitespace")
    String passwordHash,

    @Pattern(regexp = "\\S.*", message = "must not be empty or whitespace")
    String title,

    Boolean isActive,

    Boolean isOwner,

    Boolean isDeleted
) {

}
