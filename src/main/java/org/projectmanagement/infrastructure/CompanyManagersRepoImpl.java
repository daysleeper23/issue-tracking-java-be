package org.projectmanagement.infrastructure;

import lombok.RequiredArgsConstructor;
import org.projectmanagement.domain.repository.CompanyManagersRepoJpa;
import org.projectmanagement.domain.entities.CompanyManagers;
import org.projectmanagement.domain.repository.CompanyManagersRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class CompanyManagersRepoImpl implements CompanyManagersRepository {

    private final CompanyManagersRepoJpa companyManagersRepoJpa;

    @Override
    public CompanyManagers save(CompanyManagers companyManager) {
        return companyManagersRepoJpa.save(companyManager);
    }

    @Override
    public void deleteById(UUID id) {
        companyManagersRepoJpa.deleteById(id);
    }

    @Override
    public List<CompanyManagers> findAllFromCompany(UUID companyId) {
        return companyManagersRepoJpa.findAllByCompanyId(companyId);
    }

    @Override
    public Optional<CompanyManagers> findById(UUID id) {
        return companyManagersRepoJpa.findById(id);
    }

    @Override
    public Optional<CompanyManagers> findByUserId(UUID userId) {
        return companyManagersRepoJpa.findByUserId(userId);
    }
}
