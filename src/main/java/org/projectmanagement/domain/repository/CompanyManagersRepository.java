package org.projectmanagement.domain.repository;

import org.projectmanagement.domain.entities.CompanyManagers;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CompanyManagersRepository {
    CompanyManagers save(CompanyManagers companyManager);

    void deleteById(UUID id);

    List<CompanyManagers> findAllFromCompany(UUID companyId);

    Optional<CompanyManagers> findById(UUID id);

    Optional<CompanyManagers> findByUserId(UUID userId);

}
