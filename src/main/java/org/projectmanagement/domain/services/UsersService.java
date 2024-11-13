package org.projectmanagement.domain.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.projectmanagement.application.dto.users.UsersCreate;
import org.projectmanagement.application.dto.users.UsersRead;

public interface UsersService {

  UsersRead createUser(UsersCreate user);

  Optional<UsersRead> getUserById(UUID id);

  List<UsersRead> getAllUsersOfCompany(UUID companyId);

  UsersRead updateUser(UUID id, UsersCreate user);

  void deleteUser(UUID id);
}
