package org.projectmanagement.application.services;

import org.projectmanagement.application.dto.users.UsersAuth;
import org.projectmanagement.application.dto.users.UsersLogin;
import org.projectmanagement.application.exceptions.AppMessage;
import org.projectmanagement.application.exceptions.ApplicationException;
import org.projectmanagement.domain.entities.Users;
import org.projectmanagement.domain.repository.UsersRepository;
import org.projectmanagement.domain.services.AuthService;
import org.projectmanagement.presentation.config.JwtHelper;
import org.springframework.http.HttpStatus;
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

        String token = authenticate(user);
        return UsersAuth.builder()
                .id(existingUser.get().getId())
                .email(existingUser.get().getEmail())
                .companyId(existingUser.get().getCompanyId())
                .token(token)
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
