package org.projectmanagement.domain.repository;

import org.projectmanagement.domain.entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RolesRepository extends JpaRepository<Roles, UUID> {

    @Query("SELECT r FROM Roles r WHERE r.name = :name AND r.companyId = :companyId")
    Optional<Roles> findByExactName(String name, UUID companyId);

    @Query("SELECT r FROM Roles r WHERE r.companyId = :companyId")
    List<Roles> findAllRolesOfCompany(UUID companyId);

    @Query("DELETE FROM Roles r WHERE r.id = :id")
    void deleteById(UUID id);
}
