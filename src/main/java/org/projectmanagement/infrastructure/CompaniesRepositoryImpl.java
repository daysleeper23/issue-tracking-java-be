package org.projectmanagement.infrastructure;

import org.projectmanagement.domain.entities.Companies;
import org.projectmanagement.domain.repository.CompaniesRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.stream.IntStream;

@Repository
public class CompaniesRepositoryImpl implements CompaniesRepository {

    private InMemoryDatabase inMemoryDatabase;

    public CompaniesRepositoryImpl(InMemoryDatabase inMemoryDatabase) {
        this.inMemoryDatabase = inMemoryDatabase;
    }

    @Override
    public Companies save(Companies companies) {
        if(companies.getId() == null){
            companies.setId(UUID.randomUUID());
            inMemoryDatabase.getCompanies().add(companies);
        }else {
            int index = IntStream.range(0,inMemoryDatabase.getCompanies().size()).filter(i->
                    inMemoryDatabase.getCompanies().get(i).getId() == companies.getId()).findFirst().orElse(-1);
            inMemoryDatabase.getCompanies().set(index,companies);
        }
        return companies;
    }

    @Override
    public Companies findOne(UUID companyId) {
        return inMemoryDatabase.getCompanies().stream().filter(c-> c.getId().equals(companyId)).findFirst().orElse(null);
    }

}
