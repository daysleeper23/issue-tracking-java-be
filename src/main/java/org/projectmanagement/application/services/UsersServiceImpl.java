package org.projectmanagement.application.services;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.projectmanagement.application.dto.users.*;
import org.projectmanagement.domain.entities.Users;
import org.projectmanagement.domain.repository.UsersRepository;
import org.projectmanagement.domain.services.UsersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;

    @Autowired
    public UsersServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public OwnersRead createOwner(OwnersCreate ownersCreate) {
        Users newUser = usersRepository.save(
                Users.builder()
                        .name(ownersCreate.name())
                        .email(ownersCreate.email())
                        .passwordHash(ownersCreate.passwordHash())
                        .isActive(true)
                        .isOwner(true)
                        .isDeleted(false)
                        .createdAt(Instant.now())
                        .updatedAt(Instant.now())
                        .build()
        );

        return new OwnersRead(newUser.getId(), newUser.getName(), newUser.getEmail());
    }

    public UsersRead createUser(UsersCreate user) {
        Users newUser = usersRepository.save(
               Users.builder()
                        .name(user.name())
                        .email(user.email())
                        .passwordHash(user.passwordHash())
                        .title(user.title())
                        .isActive(user.isActive())
                        .companyId(user.companyId())
                        .isOwner(false)
                        .isDeleted(false)
                        .createdAt(Instant.now())
                        .updatedAt(Instant.now())
                        .build()
        );

        return UsersMapper.toUsersRead(newUser);
    }

    public Optional<UsersRead> getUserById(UUID id) {
        Users users = usersRepository.findById(id).orElse(null);
        if (users == null) {
            return Optional.empty();
        }
        return Optional.of(UsersMapper.toUsersRead(users));
    }

    public List<UsersRead> getAllUsersOfCompany(UUID companyId) {
        List<Users> users = usersRepository.findAllFromCompany(companyId);
        return users.stream().map(UsersMapper::toUsersRead).toList();
    }

    public Boolean deleteUser(UUID id) {
        usersRepository.deleteById(id);
        return true;
    }

    public UsersRead updateUser(UUID id, UsersUpdate user) {
        Users existingUser = usersRepository.findById(id).orElse(null);

        if (existingUser == null) {
            return null;
        }

        UsersMapper.INSTANCE.toUsersFromUsersUpdate(user, existingUser);
        Users updatedUser = usersRepository.save(existingUser);
        return UsersMapper.toUsersRead(updatedUser);
    }
}
