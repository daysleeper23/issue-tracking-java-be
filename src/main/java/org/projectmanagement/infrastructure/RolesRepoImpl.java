package org.projectmanagement.infrastructure;

import org.projectmanagement.domain.repository.RolesRepoJpa;
import org.projectmanagement.domain.repository.RolesRepository;
import org.springframework.stereotype.Repository;

import org.projectmanagement.domain.entities.Roles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RolesRepoImpl implements RolesRepository {
    private final RolesRepoJpa jpaRepo;

    public RolesRepoImpl(RolesRepoJpa rolesRepoJpa) {
        this.jpaRepo = rolesRepoJpa;
    }

    public Roles save(Roles role) {
        return jpaRepo.save(role);
    }

    public Optional<Roles> findByExactName(String name, UUID companyId) {
        return jpaRepo.findByExactName(name, companyId);
    }

    public Optional<Roles> findById(UUID id) {
        return jpaRepo.findById(id);
    }

    public void deleteById(UUID id) {
        jpaRepo.deleteById(id);
    }

    public List<Roles> findAllRolesOfCompany(UUID companyId) {
        return jpaRepo.findAllByCompanyId(companyId);
    }
}
