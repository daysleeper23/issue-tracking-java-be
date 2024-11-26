package org.projectmanagement.test_data_factories;

import org.projectmanagement.domain.entities.Companies;
import org.projectmanagement.domain.entities.Users;
import org.projectmanagement.domain.repository.UsersRepoJpa;
import org.projectmanagement.domain.repository.jpa.CompaniesJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UsersDataFactory {
    @Autowired
    private UsersRepoJpa usersRepoJpa;
    @Autowired
    private CompaniesJpaRepository companiesJpaRepository;

    public void deleteAll() {
        usersRepoJpa.deleteAll();
    }


    public UUID createOwnerUser(UUID companyId, String email, String password) {
        Users user = usersRepoJpa.save(Users.builder()
                .name("Test User")
                .email(email)
                .passwordHash(password)
                .companyId(companyId)
                .isActive(true)
                .isOwner(true)
                .isDeleted(false)
                .build());

        Companies company = companiesJpaRepository.findById(companyId).orElse(null);

        company.setOwnerId(user.getId());

        companiesJpaRepository.save(company);

        return user.getId();
    }

    public UUID createNonOwnerUser(UUID companyId) {
        Users user = usersRepoJpa.save(Users.builder()
                .name("Test User2")
                .email("testuser2" + UUID.randomUUID().toString() + "@example.com")
                .passwordHash("hashedpassword")
                .companyId(companyId)
                .isActive(true)
                .isOwner(false)
                .isDeleted(false)
                .build());

        return user.getId();
    }

    public UUID createNonOwnerUser(UUID companyId,String userName, String password) {
        Users user = usersRepoJpa.save(Users.builder()
                .name("Test User2")
                .email(userName)
                .passwordHash(password)
                .companyId(companyId)
                .isActive(true)
                .isOwner(false)
                .isDeleted(false)
                .build());

        return user.getId();
    }
}
