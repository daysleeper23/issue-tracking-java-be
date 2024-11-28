package org.projectmanagement.application.services;

import org.projectmanagement.application.exceptions.AppMessage;
import org.projectmanagement.application.exceptions.ApplicationException;
import org.projectmanagement.domain.entities.Users;
import org.projectmanagement.domain.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UsersRepository usersRepository;
    private final CompanyManagersRepository cmRepository;
    private final WorkspacesMembersRolesRepository wmrRepository;
    private final RolesPermissionsRepository rpRepository;
    private final PermissionsRepoJpa pjRepository;
    private final ProjectMembersRepository pmRepository;

    public UserDetailsServiceImpl(
            UsersRepository ur
            , CompanyManagersRepository cmr
            , RolesPermissionsRepository rpr
            , PermissionsRepoJpa pjr
            , WorkspacesMembersRolesRepository wmrr
            , ProjectMembersRepository pmr) {
        this.usersRepository = ur;
        this.cmRepository = cmr;
        this.rpRepository = rpr;
        this.pjRepository = pjr;
        this.wmrRepository = wmrr;
        this.pmRepository = pmr;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> user = usersRepository.findOneByEmail(username);
        if (user.isEmpty()) {
            throw new ApplicationException(HttpStatus.NOT_FOUND, AppMessage.USER_NOT_FOUND);
        }

        //retrieve user roles and permissions
        List<GrantedAuthority> authorities = new ArrayList<>();

        //add company id as permission
        if (user.get().getCompanyId() != null) {
            authorities.add(user.get().getCompanyId()::toString);
        }

        //add permissions if user is company managers or admin
        populateCompanyPermissions(authorities, user.get().getId());

        //add permissions if user is member of a workspace
        populateWorkspacePermissions(authorities, user.get().getId());

        //add permissions if user is member of a project
        populateProjectPermissions(authorities, user.get().getId());

        System.out.println("- Final Authorities: ");
        authorities.forEach(a -> System.out.println("--- " + a.getAuthority()));
        System.out.println("- End Final Authorities: ");

        return user.map(users -> User.builder()
                .username(users.getEmail())
                .password(users.getPasswordHash())
                .authorities(authorities)
                .build()).orElse(null);
    }

    private void populateCompanyPermissions(List<GrantedAuthority> authorities, UUID userId) {
        //Check if user is company manager or admin
        cmRepository.findByUserId(userId)
            //add permissions if user is company managers or admin
            .ifPresent(cm ->
                rpRepository.findAllPermissionsOfRoleByRoleId(cm.getRoleId()).forEach(rp ->
                    pjRepository.findById(rp).ifPresent(p -> authorities.add(p::getName))
                )
            );
    }

    private void populateWorkspacePermissions(List<GrantedAuthority> authorities, UUID userId) {
        //Check if user is member of a workspace  - concat WORKSPACE_XXX_ONE with workspaceId
        wmrRepository.findByUserId(userId)
            //add permissions if user has roles in some workplaces
            .ifPresent(wmr ->
                rpRepository.findAllPermissionsOfRoleByRoleId(wmr.getRoleId()).forEach(rp ->
                    pjRepository.findById(rp).ifPresent(p -> {
                        //check if the permission is already added
                        if (authorities.stream().anyMatch(a -> a.getAuthority().equals(p.getName()))) {
                            return;
                        }

                        if (p.getName().contains("WORKSPACE_") && p.getName().contains("_ONE")) {
                            authorities.add(p.getName()::toString);
                            authorities.add((p.getName() + "_" + wmr.getWorkspaceId())::toString);
                        } else {
                            authorities.add(p.getName()::toString);
                        }
                    })
                )
            );
    }

    private void populateProjectPermissions(List<GrantedAuthority> authorities, UUID userId) {
        List<GrantedAuthority> projectAuth = new ArrayList<>();

        //Check if user is member of a project - concat PROJECT_XXX_ONE with project Id
        pmRepository.findAllProjectsMemberIsPartOfByUserId(userId)
            //add project permissions if user has roles in some projects
            .forEach(pm -> {
                String projectId = pm.getProjectId().toString();
                authorities.forEach(a -> {
                    if (a.getAuthority().contains("PROJECT_") && a.getAuthority().contains("_ONE")) {
                        projectAuth.add((a.getAuthority() + "_" + projectId)::toString);
                    }
                });
            });

        authorities.addAll(projectAuth);
    }
}
