package org.projectmanagement.application.services;

import org.projectmanagement.domain.entities.Users;
import org.projectmanagement.domain.repository.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UsersRepository usersRepository;
    private final CompanyManagersRepository cmRepository;
    private final WorkspacesMembersRolesRepository wmrRepository;
    private final RolesPermissionsRepository rpRepository;
    private final PermissionsJpaRepo pjRepository;

    public UserDetailsServiceImpl(
            UsersRepository ur
            , CompanyManagersRepository cmr
            , RolesPermissionsRepository rpr
            , PermissionsJpaRepo pjr
            , WorkspacesMembersRolesRepository wmrr) {
        this.usersRepository = ur;
        this.cmRepository = cmr;
        this.rpRepository = rpr;
        this.pjRepository = pjr;
        this.wmrRepository = wmrr;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> user = usersRepository.findOneByEmail(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        //retrieve user roles and permissions
        List<GrantedAuthority> authorities = new ArrayList<>();

        //Check if user is company manager or admin
        cmRepository.findByUserId(user.get().getId())
            //add permissions if user is company managers or admin
            .ifPresent(cm ->
                rpRepository.findAllPermissionsOfRoleByRoleId(cm.getRoleId()).forEach(rp ->
                    pjRepository.findById(rp).ifPresent(p -> authorities.add(p::getName))
                )
            );

        //Check if user is member of a workspace
        wmrRepository.findByUserId(user.get().getId())
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
                        }
                    })
                )
            );

        authorities.forEach(a -> System.out.println("Final Authority: " + a.getAuthority()));

        return user.map(users -> User.builder()
                .username(users.getEmail())
                .password(users.getPasswordHash())
                .authorities(authorities)
                .build()).orElse(null);
    }

//    public UserDetails loadUserAndAuthByUsername(String username, UUID workspaceId) throws UsernameNotFoundException {
//        Optional<User> user = authRepo.findOneByUsername(username);
//        if (user.isEmpty()) {
//            // TODO: check security config for exceptions
//            throw new UsernameNotFoundException("User not found");
//        }
//
//        List<GrantedAuthority> authorities = new ArrayList<>();
//
//        authRepo.findWorkspaceUser().stream()
//                .filter(workspaceUser -> workspaceUser.getUserId().equals(user.get().getId()) && workspaceUser.getWorkspaceId().equals(workspaceId))
//                .forEach(workspaceUser -> {
//                    authRepo.findRolePermissions().stream()
//                            .filter(rolePermission -> rolePermission.getRole().getId().equals(workspaceUser.getRoleId()))
//                            .forEach(rolePermission -> {
//                                authRepo.findPermissions().stream()
//                                        .filter(permission -> permission.getId().equals(rolePermission.getPermission().getId()))
//                                        .forEach(permission -> {
//                                            authorities.add(permission::getName);
//                                        });
//                            });
//                });
//
//        user.get().setAuthorities(authorities);
//
//
//        return org.springframework.security.core.userDetails.User
//                .builder()
//                .username(user.get().getUsername())
//                .password(user.get().getPassword())
//                .authorities(authorities)
//                .build();
//    }
}
