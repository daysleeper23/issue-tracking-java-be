package org.projectmanagement.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.projectmanagement.domain.entities.Users;
import org.springframework.stereotype.Repository;

public interface UsersRepository {

  Users save(Users user);

  List<Users> findAllFromCompany(UUID companyId);

  Optional<Users> findById(UUID id);

  void deleteById(UUID id);
}