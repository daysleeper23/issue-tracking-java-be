package org.projectmanagement.application.services;

import org.projectmanagement.application.dto.company_managers.CompanyManagerMapper;
import org.projectmanagement.application.dto.company_managers.CreateCompanyManagersDTO;
import org.projectmanagement.application.dto.company_managers.UpdateCompanyManagersDTO;
import org.projectmanagement.domain.entities.Companies;
import org.projectmanagement.domain.entities.CompanyManagers;
import org.projectmanagement.domain.entities.Roles;
import org.projectmanagement.domain.exceptions.InvalidInputException;
import org.projectmanagement.domain.exceptions.ResourceNotFoundException;
import org.projectmanagement.domain.repository.CompaniesRepository;
import org.projectmanagement.domain.repository.CompanyManagersRepository;
import org.projectmanagement.domain.repository.RolesRepository;
import org.projectmanagement.domain.services.CompanyManagersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CompanyManagersServiceImpl implements CompanyManagersService {

    private final CompanyManagersRepository companyManagersRepository;
//    private final RolesRepository rolesRepository;
    private final CompaniesRepository companiesRepository;

    @Autowired
    CompanyManagersServiceImpl(
        CompanyManagersRepository companyManagersRepository,
//        RolesRepository rolesRepository,
        CompaniesRepository companiesRepository
    ) {
        this.companyManagersRepository = companyManagersRepository;
//        this.rolesRepository = rolesRepository;
        this.companiesRepository = companiesRepository;
    }

    @Override
    public CompanyManagers createCompanyManager(CreateCompanyManagersDTO dto) {
        return companyManagersRepository.save(
                CompanyManagers.builder()
                        .id(UUID.randomUUID())
                        .userId(dto.userId())
                        .companyId(dto.companyId())
                        .roleId(dto.roleId())
                        .createdAt(Instant.now())
                        .updatedAt(Instant.now())
                        .build()
        );
    }

    @Override
    public CompanyManagers updateCompanyManager(UUID id, UpdateCompanyManagersDTO dto) {
//        Roles role = rolesRepository.findById(dto.roleId()).orElseThrow(() -> new ResourceNotFoundException("Role with id: " + id + " is not found"));

//        if (!role.getName().equals("ADMIN") && !role.getName().equals("COMPANY_MANAGER")) {
//            throw new InvalidInputException("Company Managers can only be assigned to ADMIN or COMPANY_MANAGER roles.");
//        }

        CompanyManagers companyManagerToUpdate = companyManagersRepository.findById(id).orElse(null);
        if (companyManagerToUpdate == null) {
           throw new ResourceNotFoundException("Company Manager with id: " + id + "is not found");
        }

        CompanyManagerMapper.INSTANCE.updateCompanManagersDTOToCompanyManagers(dto, companyManagerToUpdate);

        return companyManagersRepository.save(companyManagerToUpdate);
    }

    @Override
    public CompanyManagers getById(UUID id) {
        return companyManagersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company Manager with id: " + id + " is not found"));
    }

    @Override
    public CompanyManagers getByUserId(UUID userId) {
        return companyManagersRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Company Manager with user id: " + userId + " is not found"));
    }

    @Override
    public List<CompanyManagers> getAllManagersByCompanyId(UUID companyId) {
        Companies companyFromDB = companiesRepository.findOne(companyId);
        if (companyFromDB == null) {
            throw new ResourceNotFoundException("Company with id: " + companyId + " was not found.");
        }
        return companyManagersRepository.findAllFromCompany(companyId);
    }

    @Override
    public void deleteById(UUID id) {
        companyManagersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company Manager with id: " + id + " is not found"));
        companyManagersRepository.deleteById(id);
    }
}
