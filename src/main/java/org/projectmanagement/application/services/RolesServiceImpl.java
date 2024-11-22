package org.projectmanagement.application.services;

import jakarta.transaction.Transactional;
import org.projectmanagement.application.dto.roles.RolesCreate;
import org.projectmanagement.domain.entities.Roles;
import org.projectmanagement.domain.repository.RolesRepository;
import org.projectmanagement.domain.services.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RolesServiceImpl implements RolesService {
    private final RolesRepository rolesRepository;

    @Autowired
    public RolesServiceImpl(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    public Roles createRole(UUID companyId, RolesCreate role) {
        Optional<Roles> roles = rolesRepository.findByExactName(role.getName(), companyId);
        if (roles.isEmpty()) {
            return rolesRepository.save(
                    Roles.builder()
                    .name(role.getName())
                    .companyId(companyId)
                    .isDeleted(false)
                    .isSystemRole(false)
                    .createdAt(Instant.now())
                    .updatedAt(Instant.now())
                    .build()
            );
        } else {
            return null;
        }
    }

    @Transactional
    public Roles updateRoleName(UUID id, UUID companyId, RolesCreate role) {
        Optional<Roles> existingRole = rolesRepository.findById(id);

        Optional<Roles> rolesWithSameName = rolesRepository.findByExactName(role.getName(), companyId);
        if (rolesWithSameName.isEmpty() && existingRole.isPresent()) {
            existingRole.get().setName(role.getName());
            return rolesRepository.save(existingRole.get());
        } else {
            return null;
        }
    }

    @Transactional
    public Boolean deleteRole(UUID id) {
        rolesRepository.deleteById(id);
        //considering update the status to archived instead of deleting
        return true;
    }

    public Optional<Roles> findById(UUID id) {
        return rolesRepository.findById(id);
    }

    public Optional<Roles> findByExactName(String name, UUID companyId) {
        return rolesRepository.findByExactName(name, companyId);
    }

    public List<Roles> findAllRoles(UUID companyId) {
        return rolesRepository.findAllRolesOfCompany(companyId);
    }
}
