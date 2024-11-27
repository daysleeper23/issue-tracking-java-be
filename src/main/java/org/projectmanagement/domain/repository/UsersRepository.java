package org.projectmanagement.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import org.projectmanagement.domain.entities.Users;

public interface UsersRepository {

  Users save(Users user);

  List<Users> findAllFromCompany(UUID companyId);

  Optional<Users> findById(UUID id);

  void deleteById(UUID id);

  Optional<Users> findOneByEmail(@NotBlank(message = "cannot be blank") String email);
}