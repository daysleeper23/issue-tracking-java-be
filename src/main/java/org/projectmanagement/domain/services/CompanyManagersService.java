package org.projectmanagement.domain.services;

import org.projectmanagement.application.dto.company_managers.CreateCompanyManagersDTO;
import org.projectmanagement.application.dto.company_managers.UpdateCompanyManagersDTO;
import org.projectmanagement.domain.entities.CompanyManagers;

import java.util.List;
import java.util.UUID;

public interface CompanyManagersService {
    CompanyManagers createCompanyManager(CreateCompanyManagersDTO dto);

    CompanyManagers updateCompanyManager(UUID id, UpdateCompanyManagersDTO dto);

    CompanyManagers getById(UUID id);

    CompanyManagers getByUserId(UUID userId);

    List<CompanyManagers> getAllManagersByCompanyId(UUID companyId);

    void deleteById(UUID id);
}
