package org.projectmanagement.application.services;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.validation.Valid;
import org.projectmanagement.application.dto.users.UsersCreate;
import org.projectmanagement.application.dto.users.UsersMapper;
import org.projectmanagement.application.dto.users.UsersRead;
import org.projectmanagement.domain.entities.Users;
import org.projectmanagement.domain.repository.UsersRepository;
import org.projectmanagement.domain.services.UsersService;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

@Service
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;

    public UsersServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public UsersRead createUser(UsersCreate user) {
        Users newUser = usersRepository.save(
                new Users(
                        UUID.randomUUID(),
                        user.getName(),
                        user.getEmail(),
                        user.getPasswordHash(),
                        user.getTitle(),
                        user.getIsActive(),
                        user.getCompanyId(),
                        user.getIsOwner(),
                        Instant.now(),
                        Instant.now()
                )
        );

        return UsersMapper.toUsersRead(newUser);
    }

    public Optional<UsersRead> getUserById(@Valid UUID id) {
        Users users = usersRepository.findById(id).orElse(null);
        if (users == null) {
            return Optional.empty();
        }
        return Optional.of(UsersMapper.toUsersRead(users));
    }

    public List<UsersRead> getAllUsersOfCompany(@Valid UUID companyId) {
        List<Users> users = usersRepository.findAllFromCompany(companyId);
        return users.stream().map(UsersMapper::toUsersRead).toList();
    }

    public void deleteUser(@Valid UUID id) {
        usersRepository.deleteById(id);
    }

    public UsersRead updateUser(@NotNull UUID id, @NotNull UsersCreate user) {
        Users existingUser = usersRepository.findById(id).orElse(null);

        if (existingUser == null) {
            return null;
        }

        UsersMapper.INSTANCE.toUsersFromUsersCreate(user, existingUser);
        Users updatedUser = usersRepository.save(existingUser);
        return UsersMapper.toUsersRead(updatedUser);
    }
}
