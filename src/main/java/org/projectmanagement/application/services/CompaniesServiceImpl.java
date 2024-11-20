package org.projectmanagement.application.services;

import lombok.RequiredArgsConstructor;
import org.projectmanagement.application.dto.companies.Company;
import org.projectmanagement.application.dto.company_managers.CreateCompanyManagers;
import org.projectmanagement.application.exceptions.AppMessage;
import org.projectmanagement.application.exceptions.ApplicationException;
import org.projectmanagement.domain.entities.Companies;
import org.projectmanagement.domain.entities.Users;
import org.projectmanagement.domain.repository.CompaniesRepository;
import org.projectmanagement.domain.repository.UsersRepository;
import org.projectmanagement.domain.services.CompaniesService;
import org.projectmanagement.domain.services.CompanyManagersService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CompaniesServiceImpl implements CompaniesService {

    private final CompaniesRepository companiesRepository;
    private final UsersRepository usersRepository;
    private final CompanyManagersService companyManagersService;

    @Override
    @Transactional
    public Companies createNewCompany(Company dto) {
        /*Todo: Implement security context holder
            Get userId from actual security context holder
            Check if user already joined a company or own a company
         */
        Optional<Users> checkUser = usersRepository.findById(UUID.fromString(dto.userId()));
        if (checkUser.isEmpty()){
            throw new ApplicationException(AppMessage.USER_NOT_FOUND);
        }
        if (checkUser.get().getCompanyId() != null){
            throw new ApplicationException(AppMessage.USER_ALREADY_JOINED_COMPANY);
        }
        Companies companies = new Companies();
        companies.setName(dto.name());
        companies.setDescription(dto.description());
        companies.setOwnerId(UUID.randomUUID());
        Companies saved = companiesRepository.save(companies);
        Users updateUser = checkUser.get();
        updateUser.setCompanyId(saved.getId());
        usersRepository.save(updateUser);
        //Get admin role here
        companyManagersService.createCompanyManager(new CreateCompanyManagers(
                updateUser.getId(),
                saved.getId(),
                UUID.randomUUID()
        ));
        return saved;
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
