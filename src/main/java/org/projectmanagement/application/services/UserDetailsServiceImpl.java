package org.projectmanagement.application.services;

import org.projectmanagement.application.dto.users.UsersLogin;
import org.projectmanagement.application.dto.users.UsersRead;
import org.projectmanagement.domain.entities.Users;
import org.projectmanagement.domain.repository.UsersRepository;
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

    public UserDetailsServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> user = usersRepository.findOneByEmail(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        return user.map(users -> User.builder()
                .username(users.getEmail())
                .password(users.getPasswordHash())
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
//        return org.springframework.security.core.userdetails.User
//                .builder()
//                .username(user.get().getUsername())
//                .password(user.get().getPassword())
//                .authorities(authorities)
//                .build();
//    }
}
