package org.projectmanagement.domain.services;

import org.projectmanagement.application.dto.companies.CompanyDTO;
import org.projectmanagement.domain.entities.Companies;

public interface CompaniesService {
    Companies createNewCompany(CompanyDTO dto);

    Companies getCompany(String UUID);

    Companies updateCompany(String id, CompanyDTO dto);

    boolean archiveCompany(String companyId);

}
