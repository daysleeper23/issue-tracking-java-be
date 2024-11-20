package org.projectmanagement.application.dto.company_managers;

import lombok.*;

import java.util.UUID;

public record CreateCompanyManagers(
    @NonNull
    UUID userId,
    @NonNull
    UUID companyId,
    @NonNull
    UUID roleId
) {
}
