package org.projectmanagement.domain.services;

import org.projectmanagement.application.dto.company_managers.CreateCompanyManagers;
import org.projectmanagement.application.dto.company_managers.UpdateCompanyManagers;
import org.projectmanagement.domain.entities.CompanyManagers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CompanyManagersService {
    CompanyManagers createCompanyManager(CreateCompanyManagers dto);

    CompanyManagers updateCompanyManager(UUID id, UpdateCompanyManagers dto);

    CompanyManagers getById(UUID id);

    Optional<CompanyManagers> getByUserId(UUID userId);

    List<CompanyManagers> getAllManagersByCompanyId(UUID companyId);

    void deleteById(UUID id);
}
