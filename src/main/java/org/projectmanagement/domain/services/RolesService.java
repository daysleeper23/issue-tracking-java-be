package org.projectmanagement.domain.services;

import org.projectmanagement.application.dto.roles.RolesCreate;
import org.projectmanagement.domain.entities.Roles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RolesService {
    Roles createRole(RolesCreate rc);

    Roles updateRoleName(Roles role);

    void deleteRole(UUID id);

    Optional<Roles> findByExactName(String name);
    Optional<Roles> findById(UUID id);

    List<Roles> findAllRoles();
}
