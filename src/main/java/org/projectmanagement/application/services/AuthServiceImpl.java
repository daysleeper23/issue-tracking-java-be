package org.projectmanagement.application.services;

import org.projectmanagement.application.dto.users.UsersAuth;
import org.projectmanagement.application.dto.users.UsersLogin;
import org.projectmanagement.application.dto.workspaces.WorkspacesRead;
import org.projectmanagement.application.dto.workspacesmembersroles.WorkspacesMembersRolesRead;
import org.projectmanagement.application.exceptions.AppMessage;
import org.projectmanagement.application.exceptions.ApplicationException;
import org.projectmanagement.domain.entities.Users;
import org.projectmanagement.domain.entities.Workspaces;
import org.projectmanagement.domain.entities.WorkspacesMembersRoles;
import org.projectmanagement.domain.repository.UsersRepository;
import org.projectmanagement.domain.repository.WorkspacesMembersRolesRepository;
import org.projectmanagement.domain.repository.WorkspacesRepository;
import org.projectmanagement.domain.services.AuthService;
import org.projectmanagement.domain.services.CompanyManagersService;
import org.projectmanagement.domain.services.WorkspacesMembersRolesService;
import org.projectmanagement.presentation.config.JwtHelper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {
    private final UsersRepository usersRepository;
    private final WorkspacesRepository wRepo;
    private final CompanyManagersService companyManagersService;
    private final WorkspacesMembersRolesRepository wmrRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtHelper jwtHelper;

    public AuthServiceImpl(
        UsersRepository usersRepository
        , WorkspacesRepository workspacesRepository
        , CompanyManagersService companyManagersService
        , WorkspacesMembersRolesRepository workspacesMembersRolesRepository
        , PasswordEncoder passwordEncoder
        , JwtHelper jwtHelper) {
        this.usersRepository = usersRepository;
        this.wRepo = workspacesRepository;
        this.companyManagersService = companyManagersService;
        this.wmrRepo = workspacesMembersRolesRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtHelper = jwtHelper;
    }

    public UsersAuth login(UsersLogin user) {
        Optional<Users> existingUser = usersRepository.findOneByEmail(user.getEmail());
        if (existingUser.isEmpty()) {
            throw new ApplicationException(HttpStatus.NOT_FOUND, AppMessage.USER_NOT_FOUND);
        }

        Users userEntity = existingUser.get();
        if (!passwordEncoder.matches(user.getPassword(), userEntity.getPasswordHash())) {
            System.out.println("Password does not match");
            throw new ApplicationException(AppMessage.INVALID_CREDENTIALS);
        }

        List<UUID> wIds = new ArrayList<UUID>();
        if (companyManagersService.getByUserId(existingUser.get().getId()) != null) {
            wIds = wRepo.findAllWorkspaces(existingUser.get().getCompanyId()).stream().map(w -> w.getId()).toList();
        }
        else {
            List<WorkspacesMembersRoles> wmrrl = wmrRepo.findByUserId(existingUser.get().getId());
            wIds = wmrrl.stream().map(wmr -> wmr.getWorkspaceId()).toList();
        }

        String token = authenticate(user);
        return UsersAuth.builder()
                .id(existingUser.get().getId())
                .email(existingUser.get().getEmail())
                .companyId(existingUser.get().getCompanyId())
                .name(existingUser.get().getName())
                .token(token)
                .workspaces(wIds)
                .defaultWorkspace(wIds.get(0))
                .build();
    }

    public String authenticate(UsersLogin user) {
        Optional<Users> existingUser = usersRepository.findOneByEmail(user.getEmail());
        if (existingUser.isEmpty()) {
            throw new ApplicationException(HttpStatus.NOT_FOUND, AppMessage.USER_NOT_FOUND);
        }

        return jwtHelper.generateToken(
                UsersAuth.builder()
                        .id(existingUser.get().getId())
                        .email(existingUser.get().getEmail())
                        .build()
        );
    }
}
