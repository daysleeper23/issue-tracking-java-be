package org.projectmanagement.domain.repository;


import org.projectmanagement.domain.entities.Companies;

import java.util.UUID;

public interface CompaniesRepository {
    Companies save(Companies companies);

    Companies findOne(UUID companyId);
}
