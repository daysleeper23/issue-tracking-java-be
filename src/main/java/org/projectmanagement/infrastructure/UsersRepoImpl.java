package org.projectmanagement.infrastructure;

import org.projectmanagement.application.dto.users.UsersLogin;
import org.projectmanagement.domain.entities.Roles;
import org.projectmanagement.domain.entities.Users;
import org.projectmanagement.domain.repository.UsersRepoJpa;
import org.projectmanagement.domain.repository.UsersRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UsersRepoImpl implements UsersRepository {
    private final UsersRepoJpa jpaRepo;

    public UsersRepoImpl(UsersRepoJpa usersRepoJpa) {
        this.jpaRepo = usersRepoJpa;
    }

    public Users save(Users user) {
        return jpaRepo.save(user);
    }

    public List<Users> findAllFromCompany(UUID companyId) {
        return jpaRepo.findAllByCompanyId(companyId);
    }

    public Optional<Users> findById(UUID id) {
        return jpaRepo.findById(id);
    }

    public Optional<Users> findByIdForCompany(UUID id, UUID companyId) {
        return jpaRepo.findByIdForCompany(id, companyId);
    }

    public void deleteById(UUID id) {
        jpaRepo.deleteById(id);
    }

    public Optional<Users> findOneByEmail(String email) {
        return jpaRepo.findOneByEmail(email);
    }
}
