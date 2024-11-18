package org.projectmanagement.infrastructure;

import org.projectmanagement.domain.entities.Roles;
import org.projectmanagement.domain.repository.RolesJpaRepo;
import org.projectmanagement.domain.repository.RolesRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RolesJpaRepoImpl implements RolesRepository {
    private final RolesJpaRepo rolesJpaRepository;

    public RolesJpaRepoImpl(RolesJpaRepo rolesJpaRepository) {
        this.rolesJpaRepository = rolesJpaRepository;
    }

    @Override
    public Roles safeCopy(Roles role) {
        return null;
    }

    @Override
    public Roles save(Roles role) {
        return rolesJpaRepository.save(role);
    }

    @Override
    public void deleteById(UUID id) {
//        rolesJpaRepository.deleteById(id);
    }

    @Override
    public Optional<Roles> findByExactName(String name, UUID companyId) {
        return Optional.empty();
    }

    @Override
    public Optional<Roles> findById(UUID id) {
        return rolesJpaRepository.findById(id);
    }

    @Override
    public List<Roles> findAllRolesOfCompany(UUID companyId) {
        return List.of();
    }
}
