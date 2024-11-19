package org.projectmanagement.domain.repository;

import org.projectmanagement.domain.entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RolesRepository {

    Roles save(Roles role);

    Optional<Roles> findById(UUID id);

    Optional<Roles> findByExactName(String name, UUID companyId);

    List<Roles> findAllRolesOfCompany(UUID companyId);

    void deleteById(UUID id);
}
