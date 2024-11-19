package org.projectmanagement.domain.repository.jpa;

import org.projectmanagement.domain.entities.Companies;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CompaniesJpaRepository extends JpaRepository<Companies, UUID> {
}
