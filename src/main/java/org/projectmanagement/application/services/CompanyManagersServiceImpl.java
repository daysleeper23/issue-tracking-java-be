package org.projectmanagement.application.services;

import org.projectmanagement.application.dto.company_managers.CompanyManagerMapper;
import org.projectmanagement.application.dto.company_managers.CreateCompanyManagers;
import org.projectmanagement.application.dto.company_managers.UpdateCompanyManagers;
import org.projectmanagement.domain.entities.Companies;
import org.projectmanagement.domain.entities.CompanyManagers;
import org.projectmanagement.domain.entities.Roles;
import org.projectmanagement.domain.entities.Users;
import org.projectmanagement.domain.exceptions.InvalidInputException;
import org.projectmanagement.domain.exceptions.ResourceNotFoundException;
import org.projectmanagement.domain.repository.CompaniesRepository;
import org.projectmanagement.domain.repository.CompanyManagersRepository;
import org.projectmanagement.domain.repository.RolesRepository;
import org.projectmanagement.domain.repository.UsersRepository;
import org.projectmanagement.domain.services.CompanyManagersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class CompanyManagersServiceImpl implements CompanyManagersService {

    private final CompanyManagersRepository companyManagersRepository;
    private final RolesRepository rolesRepository;
    private final CompaniesRepository companiesRepository;
    private final UsersRepository usersRepository;

    @Autowired
    CompanyManagersServiceImpl(
        CompanyManagersRepository companyManagersRepository,
        RolesRepository rolesRepository,
        CompaniesRepository companiesRepository,
        UsersRepository usersRepository
    ) {
        this.companyManagersRepository = companyManagersRepository;
        this.rolesRepository = rolesRepository;
        this.companiesRepository = companiesRepository;
        this.usersRepository = usersRepository;
    }

    @Transactional
    @Override
    public CompanyManagers createCompanyManager(CreateCompanyManagers dto) {
        Roles role = rolesRepository.findById(dto.roleId()).orElseThrow(() -> new ResourceNotFoundException("Role with id: " + dto.roleId() + " is not found"));
        if (!role.getName().equals(Roles.SystemRoles.ADMIN.getName()) &&
                !role.getName().equals(Roles.SystemRoles.COMPANY_MANAGER.getName())) {
            throw new InvalidInputException("Company Managers can only be assigned to ADMIN or COMPANY_MANAGER roles.");
        }

        Users user = usersRepository.findById(dto.userId()).orElseThrow(() -> new ResourceNotFoundException("User with id: " + dto.userId() + " is not found"));

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

    @Transactional
    @Override
    public CompanyManagers updateCompanyManager(UUID id, UpdateCompanyManagers dto) {
        Roles role = rolesRepository.findById(dto.roleId()).orElseThrow(() -> new ResourceNotFoundException("Role with id: " + id + " is not found"));

        if (!role.getName().equals(Roles.SystemRoles.ADMIN.getName()) &&
                !role.getName().equals(Roles.SystemRoles.COMPANY_MANAGER.getName())) {
            throw new InvalidInputException("Company Managers can only be assigned to ADMIN or COMPANY_MANAGER roles.");
        }

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
        Companies companyFromDB = companiesRepository.findById(companyId);
        if (companyFromDB == null) {
            throw new ResourceNotFoundException("Company with id: " + companyId + " was not found.");
        }
        return companyManagersRepository.findAllFromCompany(companyId);
    }

    @Transactional
    @Override
    public void deleteById(UUID id) {
        companyManagersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company Manager with id: " + id + " is not found"));
        companyManagersRepository.deleteById(id);
    }
}
