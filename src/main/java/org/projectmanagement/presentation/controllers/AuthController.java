package org.projectmanagement.presentation.controllers;

import jakarta.validation.Valid;
import org.projectmanagement.application.dto.users.*;
import org.projectmanagement.domain.services.AuthService;
import org.projectmanagement.domain.services.UsersService;
import org.projectmanagement.presentation.config.JwtHelper;
import org.projectmanagement.presentation.response.GlobalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<GlobalResponse<UsersAuth>> login(@RequestBody @Valid UsersLogin loginInfo) {
        System.out.println("Login attempt " + loginInfo.getEmail() + " " + loginInfo.getPassword());
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginInfo.getEmail(),
                            loginInfo.getPassword()
                    )
            );
            System.out.println("Login successful");
            Optional<UsersAuth> loggedInUser = authService.login(loginInfo);
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
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(
                    new GlobalResponse<>(HttpStatus.UNAUTHORIZED.value(), null)
                    , HttpStatus.UNAUTHORIZED);
        }
    }
}
