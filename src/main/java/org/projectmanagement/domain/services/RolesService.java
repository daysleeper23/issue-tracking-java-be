package org.projectmanagement.domain.services;

import org.projectmanagement.application.dto.roles.RolesCreate;
import org.projectmanagement.domain.entities.Roles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RolesService {
    Roles createRole(UUID companyId, RolesCreate rc);

    Roles updateRoleName(UUID id, UUID companyId, RolesCreate role);

    Boolean deleteRole(UUID id);

    Optional<Roles> findByExactName(String name, UUID companyId);
    Optional<Roles> findById(UUID id);

    List<Roles> findAllRoles(UUID companyId);
}
