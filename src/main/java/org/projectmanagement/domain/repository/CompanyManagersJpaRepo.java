package org.projectmanagement.domain.repository;

import org.projectmanagement.domain.entities.CompanyManagers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyManagersJpaRepo extends JpaRepository<CompanyManagers, UUID> {
    @Query(value = "SELECT * " +
            "FROM company_managers cm " +
            "WHERE cm.company_id = :companyId ",
            nativeQuery = true)
   List<CompanyManagers> findAllByCompanyId(@Param("companyId") UUID companyId);

    @Query(value = "SELECT * " +
            "FROM company_managers cm " +
            "WHERE cm.user_id = :userId ",
            nativeQuery = true)
    Optional<CompanyManagers> findByUserId(@Param("userId") UUID userId);
}
