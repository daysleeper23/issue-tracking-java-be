package org.projectmanagement.test_data_factories;

import org.projectmanagement.domain.entities.Companies;
import org.projectmanagement.domain.repository.jpa.CompaniesJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CompaniesDataFactory {

    @Autowired
    private CompaniesJpaRepository companiesRepoJpa;

    public void deleteAll() {
        companiesRepoJpa.deleteAll();
    }

    public UUID createCompany() {
        Companies company = companiesRepoJpa.save(Companies.builder()
                        .name("Test Company")
                        .description("Test Description")
                .build());
        return company.getId();
    }

    public UUID createCompany(String name) {
        Companies company = companiesRepoJpa.save(Companies.builder()
                .name(name)
                .description("Test Description")
                .build());
        return company.getId();
    }
}
