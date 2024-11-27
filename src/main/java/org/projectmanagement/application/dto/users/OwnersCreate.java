package org.projectmanagement.application.dto.users;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = false)
public record OwnersCreate (
        @NotBlank(message = "cannot be blank")
        String name,

        @NotBlank(message = "cannot be blank")
        String email,

        @NotBlank(message = "cannot be blank")
        String password
) {
}