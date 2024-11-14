package org.projectmanagement.domain.repository;

import org.projectmanagement.domain.entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RolesRepository {
    Roles safeCopy(Roles role);
    Roles save(Roles role);
    void deleteById(UUID id);

    Optional<Roles> findByExactName(String name, UUID companyId);
    Optional<Roles> findById(UUID id);
    List<Roles> findAllRolesOfCompany(UUID companyId);
}
