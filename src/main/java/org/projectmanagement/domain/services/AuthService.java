package org.projectmanagement.domain.services;

import org.projectmanagement.application.dto.users.UsersAuth;
import org.projectmanagement.application.dto.users.UsersLogin;

import java.util.Optional;

public interface AuthService {
    UsersAuth login(UsersLogin user);

    String authenticate(UsersLogin user);
}
