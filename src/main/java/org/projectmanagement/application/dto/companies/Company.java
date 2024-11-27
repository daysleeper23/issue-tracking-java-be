package org.projectmanagement.application.dto.companies;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = false)
public record Company(
        @NotBlank(message = "must not be empty") String name,
        String description,
        String userId
        ) {
}
