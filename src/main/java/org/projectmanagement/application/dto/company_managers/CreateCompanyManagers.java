package org.projectmanagement.application.dto.company_managers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = false)
public record CreateCompanyManagers(
    @NonNull
    UUID userId,
    @NonNull
    UUID companyId,
    @NonNull
    UUID roleId
) {
}
