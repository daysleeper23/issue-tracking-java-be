package org.projectmanagement.domain.repository;

import org.projectmanagement.domain.entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RolesRepoJpa extends JpaRepository<Roles, UUID> {

    @Query(value = "SELECT * " +
            "FROM Roles r " +
            "WHERE r.id = :id " +
            "AND r.company_id = :companyId " +
            "AND r.is_deleted = false"
            , nativeQuery = true
    )
    Optional<Roles> findByIdForCompany(@Param("id") UUID id, @Param("companyId") UUID companyId);

    @Query(value = "SELECT * " +
            "FROM Roles r " +
            "WHERE r.name = :name " +
            "AND r.company_id = :companyId " +
            "AND r.is_deleted = false"
            , nativeQuery = true)
    Optional<Roles> findByExactName(@Param("name") String name, @Param("companyId") UUID companyId);

    @Query(value = "SELECT * " +
            "FROM Roles r " +
            "WHERE r.company_id = :companyId " +
            "AND r.is_deleted = false"
            , nativeQuery = true
    )
    List<Roles> findAllByCompanyId(@Param("companyId") UUID companyId);

    @Modifying
    @Query(value = "UPDATE Roles w " +
            "SET is_deleted = true " +
            "WHERE w.id = :id " +
            "AND w.is_system_role = false"
            , nativeQuery = true
    )
    void deleteById(@Param("id") UUID id);
}
