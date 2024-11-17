package org.projectmanagement.application.dto.company_managers;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdateCompanyManagersDTO(
        @NotNull
        UUID roleId
) {
}
