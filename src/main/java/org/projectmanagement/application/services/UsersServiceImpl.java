package org.projectmanagement.application.services;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.transaction.Transactional;
import org.projectmanagement.application.dto.users.*;
import org.projectmanagement.application.exceptions.AppMessage;
import org.projectmanagement.application.exceptions.ApplicationException;
import org.projectmanagement.domain.entities.CompanyManagers;
import org.projectmanagement.domain.entities.Users;
import org.projectmanagement.domain.entities.WorkspacesMembersRoles;
import org.projectmanagement.domain.exceptions.ResourceNotFoundException;
import org.projectmanagement.domain.repository.CompanyManagersRepository;
import org.projectmanagement.domain.repository.UsersRepository;
import org.projectmanagement.domain.repository.WorkspacesMembersRolesRepository;
import org.projectmanagement.domain.services.UsersService;

import org.projectmanagement.presentation.config.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;

@Service
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final WorkspacesMembersRolesRepository wmrRepository;
    private final CompanyManagersRepository cmRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtHelper jwtHelper;

    @Autowired
    public UsersServiceImpl(
            UsersRepository usersRepository
            , WorkspacesMembersRolesRepository wmrr
            , CompanyManagersRepository cmr
            , PasswordEncoder pe
            , JwtHelper jwth
    ) {
        this.usersRepository = usersRepository;
        this.wmrRepository = wmrr;
        this.cmRepository = cmr;
        this.passwordEncoder = pe;
        this.jwtHelper = jwth;
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

        System.out.println("Creating owner with pwd: " + ownersCreate.password());
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

    public UsersRead createUser(UsersCreate user, UUID companyId) {

        //check if the email is already in use
        Optional<Users> existingUser = usersRepository.findOneByEmail(user.email());
        if (existingUser.isPresent()) {
            throw new ApplicationException(AppMessage.EMAIL_ALREADY_IN_USE);
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

    public UsersRead createAdminOrCompanyManagers(UsersCreate user, UUID companyId) {

        //check if the email is already in use
        Optional<Users> existingUser = usersRepository.findOneByEmail(user.email());
        if (existingUser.isPresent()) {
            throw new ApplicationException(AppMessage.EMAIL_ALREADY_IN_USE);
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

    public Optional<UsersRead> getUserById(UUID id) {
        Optional<Users> user = usersRepository.findById(id);
        if (user.isEmpty()) {
            throw new ApplicationException(AppMessage.USER_NOT_FOUND);
        }
        return Optional.of(UsersMapper.toUsersRead(user.get()));
    }

    public List<UsersRead> getAllUsersOfCompany(UUID companyId) {
        List<Users> users = usersRepository.findAllFromCompany(companyId);
        return users.stream().map(UsersMapper::toUsersRead).toList();
    }

    @Transactional
    public Boolean deleteUser(UUID id) {
        Optional<Users> userFromDb = usersRepository.findById(id);
        if (userFromDb.isEmpty()) {
            throw new ApplicationException(AppMessage.USER_NOT_FOUND);
        }

        usersRepository.deleteById(id);
        return true;
    }

    @Transactional
    public UsersRead updateUser(UUID id, UsersUpdate user) {
        Optional<Users> existingUser = usersRepository.findById(id);

        if (existingUser.isEmpty()) {
            throw new ApplicationException(AppMessage.USER_NOT_FOUND);
        }

        UsersMapper.INSTANCE.toUsersFromUsersUpdate(user, existingUser.get());
        Users updatedUser = usersRepository.save(existingUser.get());
        return UsersMapper.toUsersRead(updatedUser);
    }
}
