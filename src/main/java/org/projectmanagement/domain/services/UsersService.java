package org.projectmanagement.domain.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.projectmanagement.application.dto.users.UsersCreate;
import org.projectmanagement.application.dto.users.UsersRead;
import org.projectmanagement.application.dto.users.UsersUpdate;

public interface UsersService {

  UsersRead createUser(UsersCreate user);

  Optional<UsersRead> getUserById(UUID id);

  List<UsersRead> getAllUsersOfCompany(UUID companyId);

  UsersRead updateUser(UUID id, UsersUpdate user);

  Boolean deleteUser(UUID id);
}
