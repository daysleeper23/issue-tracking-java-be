package org.projectmanagement.presentation.controllers;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import org.projectmanagement.application.dto.users.UsersCreate;
import org.projectmanagement.application.dto.users.UsersRead;
import org.projectmanagement.application.dto.users.UsersUpdate;
import org.projectmanagement.application.exceptions.AppMessage;
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
    public ResponseEntity<GlobalResponse<UsersRead>> getUserById(
        @PathVariable UUID id
        , @PathVariable UUID companyId
    ) {
        UsersRead user = usersService.getUserByIdForCompany(id, companyId);
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.OK.value(), user), HttpStatus.OK);
    }

    @PostMapping("/admin")
    public ResponseEntity<GlobalResponse<UsersRead>> createAdminOrCompanyManager(
            @RequestBody @Valid UsersCreate user
            , @PathVariable UUID companyId) {
        UsersRead createdUser = usersService.createAdminOrCompanyManagers(user, companyId);
        return new ResponseEntity<>(
                new GlobalResponse<>(HttpStatus.CREATED.value(), createdUser), HttpStatus.CREATED
        );
    }

    @PostMapping
    public ResponseEntity<GlobalResponse<UsersRead>> createUser(
            @RequestBody @Valid UsersCreate user
            , @PathVariable UUID companyId) {
        UsersRead createdUser = usersService.createUser(user, companyId);
        return new ResponseEntity<>(
                new GlobalResponse<>(HttpStatus.CREATED.value(), createdUser), HttpStatus.CREATED
        );
    }

    // Update a user by ID
    @PatchMapping("/{id}")
    public ResponseEntity<GlobalResponse<UsersRead>> updateUser(
            @PathVariable UUID id
            , @PathVariable UUID companyId
            , @RequestBody @Valid UsersUpdate user) {
        UsersRead updatedUser = usersService.updateUser(id, companyId, user);
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.OK.value(), updatedUser), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalResponse<String>> deleteUser(
        @PathVariable UUID id
        , @PathVariable UUID companyId
    ) {
        usersService.deleteUser(id, companyId);
        return new ResponseEntity<>(
            new GlobalResponse<>(HttpStatus.OK.value(), AppMessage.USER_DELETED.getMessage()),
            HttpStatus.OK
        );
    }
}
