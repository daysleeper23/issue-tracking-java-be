package org.projectmanagement.domain.repository;

import org.projectmanagement.domain.entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RolesRepository {
    Roles save(Roles role);
    void deleteById(UUID id);

    Optional<Roles> findByExactName(String name);
    Optional<Roles> findById(UUID id);
    List<Roles> findAllRoles();
}
