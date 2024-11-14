package org.projectmanagement.presentation.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import org.projectmanagement.application.dto.users.UsersCreate;
import org.projectmanagement.application.dto.users.UsersRead;
import org.projectmanagement.application.dto.users.UsersUpdate;
import org.projectmanagement.domain.services.UsersService;
import org.projectmanagement.presentation.response.GlobalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/{companyId}/members")
public class UsersController {

    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping
    public ResponseEntity<GlobalResponse<List<UsersRead>>> getUsersOfCompany(@PathVariable UUID companyId) {
        return new ResponseEntity<>(
            new GlobalResponse<>(
                HttpStatus.OK.value(),
                usersService.getAllUsersOfCompany(companyId)
            ),
            HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlobalResponse<UsersRead>> getUserById(@PathVariable UUID id) {
        UsersRead user = usersService.getUserById(id).orElse(null);

        if (user == null) {
            return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.NOT_FOUND.value(), null), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.OK.value(), user), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<GlobalResponse<UsersRead>> createUser(@RequestBody @Valid UsersCreate user) {
        UsersRead createdUser = usersService.createUser(user);
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.CREATED.value(), createdUser), HttpStatus.CREATED);
    }

    // Update a user by ID
    @PatchMapping("/{id}")
    public ResponseEntity<GlobalResponse<UsersRead>> updateUser(@PathVariable UUID id, @RequestBody @Valid UsersUpdate user) {
        UsersRead updatedUser = usersService.updateUser(id, user);
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.OK.value(), updatedUser), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalResponse<String>> deleteUser(@PathVariable UUID id) {
        Boolean ok = usersService.deleteUser(id);
        return ok
                ? new ResponseEntity<>(
                    new GlobalResponse<>(HttpStatus.OK.value(), "User deleted"),
                    HttpStatus.OK
                )
                : new ResponseEntity<>(
                    new GlobalResponse<>(HttpStatus.NOT_FOUND.value(), null),
                    HttpStatus.NOT_FOUND
                );
    }
}
