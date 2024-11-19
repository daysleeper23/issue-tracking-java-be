package org.projectmanagement.application.dto.companies;

import jakarta.validation.constraints.NotBlank;

public record CompanyDTO(
        @NotBlank(message = "Company name must not be empty")String name,
        String description,
        String userId
        ) {
}
