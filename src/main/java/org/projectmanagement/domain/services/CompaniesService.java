package org.projectmanagement.domain.services;

import org.projectmanagement.application.dto.companies.Company;
import org.projectmanagement.domain.entities.Companies;

public interface CompaniesService {
    Companies createNewCompany(Company dto);

    Companies getCompany(String UUID);

    Companies updateCompany(String id, Company dto);

    boolean archiveCompany(String companyId);

}
