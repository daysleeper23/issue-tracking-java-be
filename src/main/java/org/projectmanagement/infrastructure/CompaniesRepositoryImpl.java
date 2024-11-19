package org.projectmanagement.infrastructure;

import lombok.RequiredArgsConstructor;
import org.projectmanagement.domain.entities.Companies;
import org.projectmanagement.domain.repository.CompaniesRepository;
import org.projectmanagement.domain.repository.jpa.CompaniesJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class CompaniesRepositoryImpl implements CompaniesRepository {

    private CompaniesJpaRepository jpaRepository;

    @Override
    public Companies save(Companies company) {
        return jpaRepository.save(company);
    }


    @Override
    public Companies findById(UUID id) {
        Optional<Companies> company = jpaRepository.findById(id);
        return company.orElse(null);
    }

}
