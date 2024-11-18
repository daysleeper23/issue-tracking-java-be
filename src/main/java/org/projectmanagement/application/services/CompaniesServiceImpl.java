package org.projectmanagement.application.services;

import org.projectmanagement.application.dto.companies.CompanyDTO;
import org.projectmanagement.application.exception.AppMessage;
import org.projectmanagement.application.exception.ApplicationException;
import org.projectmanagement.domain.entities.Companies;
import org.projectmanagement.domain.repository.CompaniesRepository;
import org.projectmanagement.domain.services.CompaniesService;
import org.springframework.context.ApplicationContextException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class CompaniesServiceImpl implements CompaniesService {

    private final CompaniesRepository companiesRepository;

    public CompaniesServiceImpl(CompaniesRepository companiesRepository) {
        this.companiesRepository = companiesRepository;
    }

    @Override
    public Companies createNewCompany(CompanyDTO dto) {
        /*Todo: Implement security context holder
            Get userId from actual security context holder
            Check if user already joined a company or own a company
         */
        UUID userId = UUID.randomUUID();
        Companies companies = new Companies(dto.name(), dto.description(),userId);
        return companiesRepository.save(companies);
    }

    @Override
    public Companies getCompany(String id) {
        //Check if user's companyId matches with provided companyId
        return companiesRepository.findOne(UUID.fromString(id));
    }

    @Override
    public Companies updateCompany(String id, CompanyDTO dto) {
        //Check if company is existed
        Companies existed = companiesRepository.findOne(UUID.fromString(id));
        if (existed == null){
            throw new ApplicationException(AppMessage.COMPANY_NOT_FOUND);
        }
        if (isNotChange(dto,existed)){
            throw new ApplicationException(AppMessage.NO_CHANGE);
        }
        existed.setName(dto.name());
        existed.setDescription(dto.description());
        existed.setUpdatedAt(Instant.now());
        return companiesRepository.save(existed);
    }

    @Override
    public boolean archiveCompany(String id) {
        //Retrieve companyId from owner and check if it matches with provided companyId
        Companies existed = companiesRepository.findOne(UUID.fromString(id));
        if (existed == null){
            //thr application exception not found
        }
        companiesRepository.save(existed);
        return true;
    }

    public boolean isNotChange(CompanyDTO updates, Companies target){
        return  updates.name().equals(target.getName()) &&
                updates.description().equals(target.getDescription());
    }
}
