package org.projectmanagement.infrastructure;

import org.projectmanagement.domain.entities.Users;
import org.projectmanagement.domain.repository.UsersJpaRepo;
import org.projectmanagement.domain.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UsersJpaRepoImpl implements UsersRepository {
    private final UsersJpaRepo usersJpaRepository;

    public UsersJpaRepoImpl(UsersJpaRepo usersJpaRepository) {
        this.usersJpaRepository = usersJpaRepository;
    }

    @Override
    public Users save(Users user) {
        return usersJpaRepository.save(user);
    }

    @Override
    public Users safeCopy(Users user) {
        return null;
    }

    @Override
    public List<Users> findAllFromCompany(UUID companyId) {
        return usersJpaRepository.findAll();
    }

    @Override
    public Optional<Users> findById(UUID id) {
        return usersJpaRepository.findById(id);
    }

    @Override
    public void deleteById(UUID id) {
//        usersJpaRepository.updateIsDeleted(id);
    }
}
