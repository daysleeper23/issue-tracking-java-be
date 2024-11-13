package org.projectmanagement.application.services;

import jakarta.validation.Valid;
import org.projectmanagement.application.dto.roles.RolesCreate;
import org.projectmanagement.domain.entities.Roles;
import org.projectmanagement.domain.repository.RolesRepository;
import org.projectmanagement.domain.services.RolesService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RolesServiceImpl implements RolesService {
    private final RolesRepository rolesRepository;

    public RolesServiceImpl(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    public Roles createRole(RolesCreate role) {
        Optional<Roles> roles = rolesRepository.findByExactName(role.getName());
        if (roles.isEmpty()) {
            Roles newRole = new Roles(
                    UUID.randomUUID(),
                    role.getName(),
                    role.getCompanyId(),
                    Instant.now(),
                    Instant.now()
            );
            return rolesRepository.save(newRole);
        } else {
            return null;
        }
    }

    public Roles updateRoleName(UUID id, RolesCreate role) {
        Roles existingRole = rolesRepository.findById(id).orElse(null);
        if (existingRole == null) {
            return null;
        }

        Optional<Roles> rolesWithSameName = rolesRepository.findByExactName(role.getName());
        if (rolesWithSameName.isEmpty()) {
            existingRole.setName(role.getName());
            return rolesRepository.save(existingRole);
        } else {
            return null;
        }
    }

    public Boolean deleteRole(UUID id) {
//        rolesRepository.deleteById(id);
        //considering update the status to archived instead of deleting
        return true;
    }

    public Optional<Roles> findById(UUID id) {
        return rolesRepository.findById(id);
    }

    public Optional<Roles> findByExactName(String name) {
        return rolesRepository.findByExactName(name);
    }

    public List<Roles> findAllRoles(UUID companyId) {
        return rolesRepository.findAllRolesOfCompany(companyId);
    }
}
