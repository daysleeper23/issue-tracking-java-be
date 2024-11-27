package org.projectmanagement.application.dto.company_managers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = false)
public record UpdateCompanyManagers(
        @NotNull
        UUID roleId
) {
}
