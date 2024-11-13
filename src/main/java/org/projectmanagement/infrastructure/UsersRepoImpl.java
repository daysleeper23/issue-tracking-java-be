package org.projectmanagement.infrastructure;

import org.projectmanagement.domain.entities.Users;
import org.projectmanagement.domain.repository.UsersRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UsersRepoImpl implements UsersRepository {
    private final InMemoryDatabase inMemoryDatabase;

    public UsersRepoImpl(InMemoryDatabase inMemoryDatabase) {
        this.inMemoryDatabase = inMemoryDatabase;
    }

    public Users save(Users user) {
        return inMemoryDatabase.saveUser(user);
    }

    public List<Users> findAllFromCompany(UUID companyId) {
        return inMemoryDatabase.users.stream()
                .filter(user -> user.getCompanyId().equals(companyId))
                .toList();
    }

    public Optional<Users> findById(UUID id) {
        return inMemoryDatabase.users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    public void deleteById(UUID id) {
        inMemoryDatabase.users.removeIf(user -> user.getId().equals(id));
    }

    public List<Users> findAll() {
        return inMemoryDatabase.getUsers();
    }
}
