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

    public RolesRepoImpl(InMemoryDatabase inMemoryDatabase) {
        this.inMemoryDatabase = inMemoryDatabase;
    }

    public Roles save(Roles role) {
        return inMemoryDatabase.saveRole(role);
    }

    public Optional<Roles> findByExactName(String name) {
        return inMemoryDatabase.roles.stream()
                .filter(role -> role.getName().equals(name))
                .findFirst();
    }

    public Optional<Roles> findById(UUID id) {
        return inMemoryDatabase.roles.stream()
                .filter(role -> role.getId().equals(id))
                .findFirst();
    }

    public void deleteById(UUID id) {
        inMemoryDatabase.roles.removeIf(role -> role.getId().equals(id));
    }

    public List<Roles> findAllRoles() {
        return inMemoryDatabase.roles;
    }
}
