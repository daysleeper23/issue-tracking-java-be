package org.projectmanagement.test_data_factories;

import org.projectmanagement.domain.entities.CompanyManagers;
import org.projectmanagement.domain.repository.CompanyManagersRepoJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CompanyManagersDataFactory {
    @Autowired
    CompanyManagersRepoJpa companyManagersRepoJpa;

    public void deleteAll(){
        companyManagersRepoJpa.deleteAll();
    }

    public UUID createCompanyManager(UUID userId,UUID roleId, UUID companyId) {
        CompanyManagers companyManager = companyManagersRepoJpa.save(CompanyManagers.builder()
                .userId(userId)
                .roleId(roleId)
                .companyId(companyId)
                .build());
        return  companyManager.getId();
    }
}
