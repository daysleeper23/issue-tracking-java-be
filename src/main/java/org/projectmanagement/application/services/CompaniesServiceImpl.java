package org.projectmanagement.application.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.projectmanagement.application.dto.companies.Company;
import org.projectmanagement.application.dto.company_managers.CreateCompanyManagers;
import org.projectmanagement.application.dto.roles_permissions.RolesPermissionsCreate;
import org.projectmanagement.application.exceptions.AppMessage;
import org.projectmanagement.application.exceptions.ApplicationException;
import org.projectmanagement.domain.entities.*;
import org.projectmanagement.domain.repository.CompaniesRepository;
import org.projectmanagement.domain.repository.PermissionsRepoJpa;
import org.projectmanagement.domain.repository.UsersRepository;
import org.projectmanagement.domain.services.CompaniesService;
import org.projectmanagement.domain.services.CompanyManagersService;
import org.projectmanagement.domain.services.RolesPermissionsService;
import org.projectmanagement.domain.services.RolesService;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class CompaniesServiceImpl implements CompaniesService {

    private final String SUPER_ADMIN = "SUPER ADMIN";

    private final CompaniesRepository companiesRepository;
    private final UsersRepository usersRepository;
    private final CompanyManagersService companyManagersService;
    private final RolesService rolesService;
    private final RolesPermissionsService rolesPermissionsService;
    private final PermissionsRepoJpa permissionsRepoJpa;
    private final TaskExecutor taskExecutor;


    @Override
    @Transactional
    public Companies createNewCompany(Company dto) {
        /*Todo: Implement security context holder
            Get userId from actual security context holder
            Check if user already joined a company or own a company
         */
        log.info("createNewCompany: {}", dto.toString());
        Optional<Users> checkUser = usersRepository.findById(UUID.fromString(dto.userId()));
        if (checkUser.isEmpty()){
            throw new ApplicationException(AppMessage.USER_NOT_FOUND);
        }
        if (checkUser.get().getCompanyId() != null){
            throw new ApplicationException(AppMessage.USER_ALREADY_JOINED_COMPANY);
        }

        Companies newCompany = new Companies(dto.name(), dto.description(),checkUser.get().getId());
        Companies saved = companiesRepository.save(newCompany);
        Users updateUser = checkUser.get();
        updateUser.setCompanyId(newCompany.getId());
        usersRepository.save(updateUser);
        addSuperAdminRole(saved.getId(), updateUser.getId());
        return newCompany;
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

    private void addSuperAdminRole(UUID companyId, UUID userId){
        List<Permissions> permissions = permissionsRepoJpa.findAll();
        List<RolesPermissions> adminRole = rolesPermissionsService.createRolePermissions(companyId,
                new RolesPermissionsCreate(Roles.SystemRoles.ADMIN.getName(),
                        permissions.stream().map(Permissions::getId).toList())
        );
        companyManagersService.createCompanyManager(new CreateCompanyManagers(
                userId,
                companyId,
                adminRole.get(0).getRoleId()
        ));
    }
}
