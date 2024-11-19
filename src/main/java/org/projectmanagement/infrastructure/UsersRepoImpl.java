package org.projectmanagement.infrastructure;

import org.projectmanagement.domain.entities.Roles;
import org.projectmanagement.domain.entities.Users;
import org.projectmanagement.domain.repository.UsersRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UsersRepoImpl{
    private final InMemoryDatabase inMemoryDatabase;

    public UsersRepoImpl(InMemoryDatabase inMemoryDatabase) {
        this.inMemoryDatabase = inMemoryDatabase;
    }

    public Users safeCopy(Users user) {
        return new Users(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getTitle(),
                user.getIsActive(),
                user.getCompanyId(),
                user.getIsOwner(),
                user.getIsDeleted(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    public Users save(Users user) {
        return safeCopy(inMemoryDatabase.saveUser(user));
    }

    public List<Users> findAllFromCompany(UUID companyId) {
        return inMemoryDatabase.getUsersByCompany(companyId).stream().map(this::safeCopy).toList();
    }

    public Optional<Users> findById(UUID id) {
        return inMemoryDatabase.users.stream()
                .filter(user -> user.getId().equals(id) && !user.getIsDeleted())
                .findFirst().map(this::safeCopy);
    }

    public void deleteById(UUID id) {
        inMemoryDatabase.deleteUserById(id);
    }
}
