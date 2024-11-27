package org.projectmanagement.application.dto.roles;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;


@JsonIgnoreProperties(ignoreUnknown = false)
public record RolesCreate(
    @NotBlank(message = "cannot be blank")
    String name
) {
}