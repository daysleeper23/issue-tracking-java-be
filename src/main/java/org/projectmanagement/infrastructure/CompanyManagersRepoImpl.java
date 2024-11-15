package org.projectmanagement.infrastructure;

import org.projectmanagement.domain.entities.CompanyManagers;
import org.projectmanagement.domain.repository.CompanyManagersRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CompanyManagersRepoImpl implements CompanyManagersRepository {

    private final InMemoryDatabase inMemoryDatabase;

    CompanyManagersRepoImpl(InMemoryDatabase inMemoryDatabase) {
        this.inMemoryDatabase = inMemoryDatabase;
    }

    @Override
    public CompanyManagers save(CompanyManagers companyManager) {
        return inMemoryDatabase.saveCompanyManager(companyManager);
    }

    @Override
    public void deleteById(UUID id) {
        inMemoryDatabase.companyManagers.removeIf(companyManager -> companyManager.getId().equals(id));
    }

    @Override
    public List<CompanyManagers> findAllFromCompany(UUID companyId) {
        return inMemoryDatabase.companyManagers.stream().filter(companyManager -> companyManager.getCompanyId().equals(companyId)).toList();
    }

    @Override
    public Optional<CompanyManagers> findById(UUID id) {
        return inMemoryDatabase.companyManagers.stream().filter(companyManager -> companyManager.getId().equals(id)).findFirst();
    }

    @Override
    public Optional<CompanyManagers> findByUserId(UUID userId) {
        return inMemoryDatabase.companyManagers.stream().filter(companyManager -> companyManager.getUserId().equals(userId)).findFirst();
    }
}
