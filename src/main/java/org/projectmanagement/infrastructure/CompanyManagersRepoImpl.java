package org.projectmanagement.infrastructure;

import lombok.RequiredArgsConstructor;
import org.projectmanagement.domain.repository.CompanyManagersJpaRepo;
import org.projectmanagement.domain.entities.CompanyManagers;
import org.projectmanagement.domain.repository.CompanyManagersRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstruct
@Repository
public class CompanyManagersRepoImpl implements CompanyManagersRepository {

    private final CompanyManagersJpaRepo companyManagersJpaRepo;

    CompanyManagersRepoImpl(CompanyManagersJpaRepo companyManagersJpaRepo) {
        this.companyManagersJpaRepo = companyManagersJpaRepo;
    }

    @Override
    public CompanyManagers save(CompanyManagers companyManager) {
        return companyManagersJpaRepo.save(companyManager);
    }

    @Override
    public void deleteById(UUID id) {
        companyManagersJpaRepo.deleteById(id);
    }

    @Override
    public List<CompanyManagers> findAllFromCompany(UUID companyId) {
        return companyManagersJpaRepo.findAllByCompanyId(companyId);
    }

    @Override
    public Optional<CompanyManagers> findById(UUID id) {
        return companyManagersJpaRepo.findById(id);
    }

    @Override
    public Optional<CompanyManagers> findByUserId(UUID userId) {
        return companyManagersJpaRepo.findByUserId(userId);
    }
}
