package org.projectmanagement.presentation.controllers;

import jakarta.validation.Valid;
import org.projectmanagement.application.dto.users.OwnersCreate;
import org.projectmanagement.application.dto.users.OwnersRead;
import org.projectmanagement.application.dto.users.UsersCreate;
import org.projectmanagement.application.dto.users.UsersRead;
import org.projectmanagement.domain.services.UsersService;
import org.projectmanagement.presentation.response.GlobalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class OwnersController {
    private final UsersService usersService;

    @Autowired
    public OwnersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/signup")
    public ResponseEntity<GlobalResponse<OwnersRead>> createOwner(@RequestBody @Valid OwnersCreate owner) {
        Optional<OwnersRead> createdUser = usersService.createOwner(owner);
        return createdUser
            .map(ownersRead ->
                new ResponseEntity<>(
                    new GlobalResponse<>(HttpStatus.CREATED.value(), ownersRead)
                    , HttpStatus.CREATED)
            )
            .orElseGet(() ->
                new ResponseEntity<>(
                    new GlobalResponse<>(HttpStatus.BAD_REQUEST.value(), null), HttpStatus.BAD_REQUEST
                )
            );
    }

    @PostMapping("/login")
    public ResponseEntity<GlobalResponse<UsersRead>> login(@RequestBody @Valid UsersCreate user) {
        Optional<UsersRead> loggedInUser = usersService.login(user);
        return loggedInUser
            .map(usersRead ->
                new ResponseEntity<>(
                    new GlobalResponse<>(HttpStatus.OK.value(), usersRead)
                    , HttpStatus.OK)
            )
            .orElseGet(() ->
                new ResponseEntity<>(
                    new GlobalResponse<>(HttpStatus.UNAUTHORIZED.value(), null)
                    , HttpStatus.UNAUTHORIZED)
            );
    }
}
