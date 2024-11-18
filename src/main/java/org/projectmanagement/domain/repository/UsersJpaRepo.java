package org.projectmanagement.domain.repository;

import org.projectmanagement.domain.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UsersJpaRepo extends JpaRepository<Users, UUID> {
}
