package org.projectmanagement.application.services;

import lombok.RequiredArgsConstructor;
import org.projectmanagement.application.dto.companies.Company;
import org.projectmanagement.application.exceptions.AppMessage;
import org.projectmanagement.application.exceptions.ApplicationException;
import org.projectmanagement.domain.entities.Companies;
import org.projectmanagement.domain.repository.CompaniesRepository;
import org.projectmanagement.domain.services.CompaniesService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CompaniesServiceImpl implements CompaniesService {

    private final CompaniesRepository companiesRepository;

    @Override
    public Companies createNewCompany(Company dto) {
        /*Todo: Implement security context holder
            Get userId from actual security context holder
            Check if user already joined a company or own a company
         */
        Companies companies = new Companies();
        companies.setName(dto.name());
        companies.setDescription(dto.description());
        companies.setOwnerId(UUID.randomUUID());
        return companiesRepository.save(companies);
    }

    @Override
    public Companies getCompany(String id) {
        Companies company = companiesRepository.findById(UUID.fromString(id));
        if (company == null){
            throw new ApplicationException(AppMessage.COMPANY_NOT_FOUND);
        }
        //Check if user's companyId matches with provided companyId
        return company;
    }

    @Override
    public Companies updateCompany(String id, Company dto) {
        //Check if company is existed
        Companies existed = companiesRepository.findById(UUID.fromString(id));
        if (existed == null){
            throw new ApplicationException(AppMessage.COMPANY_NOT_FOUND);
        }
        if (isNotChange(dto,existed)){
            throw new ApplicationException(AppMessage.NO_CHANGE);
        }
        existed.setName(dto.name());
        existed.setDescription(dto.description());
        return companiesRepository.save(existed);
    }

    @Override
    public boolean archiveCompany(String id) {
        //Retrieve companyId from owner and check if it matches with provided companyId
        Companies existed = companiesRepository.findById(UUID.fromString(id));
        if (existed == null){
            throw new ApplicationException(AppMessage.COMPANY_NOT_FOUND);
        }
//        companiesRepository.delete(existed.get());
        return true;
    }

    public boolean isNotChange(Company updates, Companies target){
        return  updates.name().equals(target.getName()) &&
                updates.description().equals(target.getDescription());
    }
}
