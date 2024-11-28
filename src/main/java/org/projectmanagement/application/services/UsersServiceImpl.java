package org.projectmanagement.application.services;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.transaction.Transactional;
import org.projectmanagement.application.dto.users.*;
import org.projectmanagement.application.exceptions.AppMessage;
import org.projectmanagement.application.exceptions.ApplicationException;
import org.projectmanagement.domain.entities.*;
import org.projectmanagement.domain.repository.*;
import org.projectmanagement.domain.services.UsersService;

import org.projectmanagement.presentation.config.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final WorkspacesRepository workspacesRepo;
    private final RolesRepository rolesRepo;
    private final WorkspacesMembersRolesRepository wmrRepository;
    private final CompanyManagersRepository cmRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtHelper jwtHelper;

    @Autowired
    public UsersServiceImpl(
            UsersRepository usersRepository
            , WorkspacesRepository wr
            , RolesRepository rr
            , WorkspacesMembersRolesRepository wmrr
            , CompanyManagersRepository cmr
            , PasswordEncoder pe
            , JwtHelper jwth
    ) {
        this.usersRepository = usersRepository;
        this.wmrRepository = wmrr;
        this.rolesRepo = rr;
        this.cmRepository = cmr;
        this.passwordEncoder = pe;
        this.jwtHelper = jwth;
        this.workspacesRepo = wr;
    }

    public Optional<UsersRead> login(UsersLogin user) {
        Optional<Users> existingUser = usersRepository.findOneByEmail(user.getEmail());
        if (existingUser.isEmpty()) {
            return Optional.empty();
        }

        Users userEntity = existingUser.get();
        if (!passwordEncoder.matches(user.getPassword(), userEntity.getPasswordHash())) {
            System.out.println("Password does not match");
            return Optional.empty();
        }

        return Optional.of(UsersMapper.toUsersRead(userEntity));
    }

    public String authenticate(UsersLogin user) {
        Optional<Users> existingUser = usersRepository.findOneByEmail(user.getEmail());
        if (existingUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        return jwtHelper.generateToken(new UsersLogin(existingUser.get().getEmail(), existingUser.get().getPasswordHash()));
    }

    public Optional<OwnersRead> createOwner(OwnersCreate ownersCreate) {
        //check if the email is already in use
        Optional<Users> existingUser = usersRepository.findOneByEmail(ownersCreate.email());
        if (existingUser.isPresent()) {
            throw new ApplicationException(AppMessage.EMAIL_ALREADY_IN_USE);
        }

        //hash the password
        String hashedPassword = passwordEncoder.encode(ownersCreate.password());

        //create a new owner
        Users newUser = usersRepository.save(
                Users.builder()
                        .name(ownersCreate.name())
                        .email(ownersCreate.email())
                        .passwordHash(hashedPassword)
                        .isActive(true)
                        .isOwner(true)
                        .isDeleted(false)
                        .createdAt(Instant.now())
                        .updatedAt(Instant.now())
                        .build()
        );

        return Optional.of(new OwnersRead(newUser.getId(), newUser.getName(), newUser.getEmail()));
    }

    @Transactional
    public UsersRead createUser(UsersCreate user, UUID companyId) {

        //check if the email is already in use
        Optional<Users> existingUser = usersRepository.findOneByEmail(user.email());
        if (existingUser.isPresent()) {
            throw new ApplicationException(AppMessage.EMAIL_ALREADY_IN_USE);
        }

        //check if the workspace exists in the company
        Optional<Workspaces> ws = workspacesRepo.findByIdForCompany(user.workspaceId(), companyId);
        if (ws.isEmpty()) {
            throw new ApplicationException(HttpStatus.NOT_FOUND, AppMessage.WORKSPACE_NOT_FOUND);
        }

        //check if the role exists in the company
        Optional<Roles> role = rolesRepo.findByIdForCompany(user.roleId(), companyId);
        if (role.isEmpty()) {
            throw new ApplicationException(HttpStatus.NOT_FOUND, AppMessage.ROLE_NOT_FOUND);
        }

        //check if the role is a member role or workspace manager role
        if (role.get().getName().equals(Roles.SystemRoles.ADMIN)
            || role.get().getName().equals(Roles.SystemRoles.COMPANY_MANAGER)
        ) {
            throw new ApplicationException(AppMessage.WMR_INVALID_ROLE);
        }

        //create a new user
        Users newUser = usersRepository.save(
               Users.builder()
                        .name(user.name())
                        .email(user.email())
                        .passwordHash(user.password())
                        .title(user.title())
                        .isActive(user.isActive() == null ? true : user.isActive())
                        .companyId(companyId)
                        .isOwner(false)
                        .isDeleted(false)
                        .createdAt(Instant.now())
                        .updatedAt(Instant.now())
                        .build()
        );

        //add the member to the workspace with the role
        wmrRepository.save(
                WorkspacesMembersRoles.builder()
                        .userId(newUser.getId())
                        .workspaceId(user.workspaceId())
                        .roleId(user.roleId())
                        .build()
        );

        return UsersMapper.toUsersRead(newUser);
    }

    @Transactional
    public UsersRead createAdminOrCompanyManagers(UsersCreate user, UUID companyId) {

        //check if the email is already in use
        Optional<Users> existingUser = usersRepository.findOneByEmail(user.email());
        if (existingUser.isPresent()) {
            throw new ApplicationException(AppMessage.EMAIL_ALREADY_IN_USE);
        }

        //check if the role exists in the company
        Optional<Roles> role = rolesRepo.findByIdForCompany(user.roleId(), companyId);
        if (role.isEmpty()) {
            throw new ApplicationException(HttpStatus.NOT_FOUND, AppMessage.ROLE_NOT_FOUND);
        }

        //check if the role is a super admin role or company managers role
        if (!role.get().getName().equals(Roles.SystemRoles.ADMIN)
            && !role.get().getName().equals(Roles.SystemRoles.COMPANY_MANAGER)
        ) {
            throw new ApplicationException(AppMessage.USER_INVALID_ROLE_FOR_ADMIN);
        }

        //create a new user
        Users newUser = usersRepository.save(
                Users.builder()
                        .name(user.name())
                        .email(user.email())
                        .passwordHash(user.password())
                        .title(user.title())
                        .isActive(user.isActive() == null ? true : user.isActive())
                        .companyId(companyId)
                        .isOwner(false)
                        .isDeleted(false)
                        .createdAt(Instant.now())
                        .updatedAt(Instant.now())
                        .build()
        );

        //add the member to the company managers list
        cmRepository.save(
            CompanyManagers.builder()
                .companyId(companyId)
                .userId(newUser.getId())
                .roleId(user.roleId())
                .build()
        );

        return UsersMapper.toUsersRead(newUser);
    }

    public UsersRead getUserByIdForCompany(UUID id, UUID companyId) {
        Optional<Users> user = usersRepository.findByIdForCompany(id, companyId);
        if (user.isEmpty()) {
            throw new ApplicationException(HttpStatus.NOT_FOUND, AppMessage.USER_NOT_FOUND);
        }
        return UsersMapper.toUsersRead(user.get());
    }

    public List<UsersRead> getAllUsersOfCompany(UUID companyId) {
        List<Users> users = usersRepository.findAllFromCompany(companyId);
        return users.stream().map(UsersMapper::toUsersRead).toList();
    }

    @Transactional
    public Boolean deleteUser(UUID id, UUID companyId) {
        Optional<Users> existingUser = usersRepository.findByIdForCompany(id, companyId);
        if (existingUser.isEmpty()) {
            throw new ApplicationException(HttpStatus.NOT_FOUND, AppMessage.USER_NOT_FOUND);
        }

        usersRepository.deleteById(id);
        return true;
    }

    @Transactional
    public UsersRead updateUser(UUID id, UUID companyId, UsersUpdate user) {
        Optional<Users> existingUser = usersRepository.findByIdForCompany(id, companyId);
        if (existingUser.isEmpty()) {
            throw new ApplicationException(HttpStatus.NOT_FOUND, AppMessage.USER_NOT_FOUND);
        }

        UsersMapper.INSTANCE.toUsersFromUsersUpdate(user, existingUser.get());
        Users updatedUser = usersRepository.save(existingUser.get());
        return UsersMapper.toUsersRead(updatedUser);
    }
}
