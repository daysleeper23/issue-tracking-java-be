package org.projectmanagement.infrastructure;

import org.projectmanagement.domain.repository.RolesRepository;
import org.springframework.stereotype.Repository;

import org.projectmanagement.domain.entities.Roles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RolesRepoImpl implements RolesRepository {
    private final InMemoryDatabase inMemoryDatabase;

    public Roles safeCopy(Roles role) {
        return new Roles(
                role.getId(),
                role.getName(),
                role.getCompanyId(),
                role.getIsDeleted(),
                role.getIsSystemRole(),
                role.getCreatedAt(),
                role.getUpdatedAt()
        );
    }

    public RolesRepoImpl(InMemoryDatabase inMemoryDatabase) {
        this.inMemoryDatabase = inMemoryDatabase;
    }

    public Roles save(Roles role) {
        return safeCopy(inMemoryDatabase.saveRole(role));
    }

    public Optional<Roles> findByExactName(String name, UUID companyId) {
        return Optional.ofNullable(inMemoryDatabase.getRoleByName(name, companyId))
                .map(this::safeCopy);
    }

    public Optional<Roles> findById(UUID id) {
        return Optional.ofNullable(inMemoryDatabase.getRoleById(id)).map(this::safeCopy);
    }

    public void deleteById(UUID id) {
        inMemoryDatabase.deleteRoleById(id);
    }

    public List<Roles> findAllRolesOfCompany(UUID companyId) {
        return inMemoryDatabase.getRolesByCompanyId(companyId).stream().map(this::safeCopy).toList();
    }
}
