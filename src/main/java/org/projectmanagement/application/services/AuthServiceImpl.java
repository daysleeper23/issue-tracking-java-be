package org.projectmanagement.application.services;

import org.projectmanagement.application.dto.users.UsersAuth;
import org.projectmanagement.application.dto.users.UsersLogin;
import org.projectmanagement.domain.entities.Users;
import org.projectmanagement.domain.repository.UsersRepository;
import org.projectmanagement.domain.services.AuthService;
import org.projectmanagement.presentation.config.JwtHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtHelper jwtHelper;

    public AuthServiceImpl(UsersRepository usersRepository, PasswordEncoder passwordEncoder, JwtHelper jwtHelper) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtHelper = jwtHelper;
    }

    public Optional<UsersAuth> login(UsersLogin user) {
        Optional<Users> existingUser = usersRepository.findOneByEmail(user.getEmail());
        if (existingUser.isEmpty()) {
            return Optional.empty();
        }

        Users userEntity = existingUser.get();
        if (!passwordEncoder.matches(user.getPassword(), userEntity.getPasswordHash())) {
            System.out.println("Password does not match");
            return Optional.empty();
        }

        String token = authenticate(user);
        return Optional.ofNullable(UsersAuth.builder()
                .id(existingUser.get().getId())
                .email(existingUser.get().getEmail())
                .token(token)
                .build());
    }

    public String authenticate(UsersLogin user) {
        Optional<Users> existingUser = usersRepository.findOneByEmail(user.getEmail());
        if (existingUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        return jwtHelper.generateToken(
                UsersAuth.builder()
                        .id(existingUser.get().getId())
                        .email(existingUser.get().getEmail())
                        .build()
        );
    }
}
