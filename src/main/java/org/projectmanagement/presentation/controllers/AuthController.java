package org.projectmanagement.presentation.controllers;

import jakarta.validation.Valid;
import org.projectmanagement.application.dto.users.*;
import org.projectmanagement.domain.services.AuthService;
import org.projectmanagement.domain.services.UsersService;
import org.projectmanagement.presentation.response.GlobalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UsersService usersService;
    private final AuthService authService;
    private final AuthenticationManager authManager;

    @Autowired
    public AuthController(UsersService usersService, AuthenticationManager am, AuthService as) {
        this.usersService = usersService;
        this.authManager = am;
        this.authService = as;
    }

    @GetMapping("/awake")
    public ResponseEntity<GlobalResponse<String>> wakeUp() {
        return new ResponseEntity<>(
            new GlobalResponse<>(HttpStatus.OK.value(), "I'm awake!")
            , HttpStatus.OK);
    }


    @PostMapping("/signup")
    public ResponseEntity<GlobalResponse<OwnersRead>> createOwner(@RequestBody @Valid OwnersCreate owner) {
        OwnersRead createdUser = usersService.createOwner(owner);
        return new ResponseEntity<>(
                    new GlobalResponse<>(HttpStatus.CREATED.value(), createdUser)
                    , HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<GlobalResponse<UsersAuth>> login(@RequestBody @Valid UsersLogin loginInfo) {
        System.out.println("Login attempt " + loginInfo.getEmail() + " " + loginInfo.getPassword());
        Authentication authentication = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginInfo.getEmail(),
                loginInfo.getPassword()
            )
        );

        System.out.println("Authentication OK!!");

        UsersAuth loggedInUser = authService.login(loginInfo);
        return new ResponseEntity<>(
            new GlobalResponse<>(HttpStatus.OK.value(), loggedInUser)
            , HttpStatus.OK);
    }
}
