package org.projectmanagement.domain.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.projectmanagement.application.dto.users.*;
import org.projectmanagement.domain.entities.Users;

public interface UsersService {

    Optional<UsersRead> login(UsersLogin user);
    String authenticate(UsersLogin user);

    Optional<OwnersRead> createOwner(OwnersCreate user);
    UsersRead createUser(UsersCreate user, UUID companyId);
    UsersRead createAdminOrCompanyManagers(UsersCreate user, UUID companyId);

    Optional<UsersRead> getUserById(UUID id);

    List<UsersRead> getAllUsersOfCompany(UUID companyId);

    UsersRead updateUser(UUID id, UsersUpdate user);

    Boolean deleteUser(UUID id);
}
