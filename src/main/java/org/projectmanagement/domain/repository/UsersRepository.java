package org.projectmanagement.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.projectmanagement.domain.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UsersRepository extends JpaRepository<Users, UUID> {

//  Users save(Users user);

//  Users safeCopy(Users user);

  @Query("SELECT u FROM Users u WHERE u.companyId = :companyId")
  List<Users> findAllFromCompany(UUID companyId);

//  @Query("SELECT u FROM Users u WHERE u.id = :id")
//  Optional<Users> findById(UUID id);

//  void deleteById(UUID id);
}