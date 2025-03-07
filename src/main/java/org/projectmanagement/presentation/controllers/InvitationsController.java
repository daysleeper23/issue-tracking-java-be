package org.projectmanagement.presentation.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.projectmanagement.application.dto.invitations.InvitationsCreate;
import org.projectmanagement.application.dto.invitations.InvitationsInfo;
import org.projectmanagement.domain.entities.Invitations;
import org.projectmanagement.domain.services.InvitationsService;
import org.projectmanagement.presentation.response.GlobalResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriBuilder;

import java.util.List;

@RestController
@RequestMapping("/{companyId}/invitations")
@RequiredArgsConstructor
public class InvitationsController {

    private final InvitationsService invitationsService;

    @PostMapping("/")
    public ResponseEntity<GlobalResponse<InvitationsInfo>> sendInvitation(
            @PathVariable String companyId,
            @RequestBody @Valid InvitationsCreate dto
    ) {
        String userId = java.util.UUID.randomUUID().toString();
        UriBuilder uriBuilder = ServletUriComponentsBuilder.fromCurrentRequest();
        return new ResponseEntity<>(new GlobalResponse<>(HttpStatus.CREATED.value(), invitationsService.sendInvitation(companyId, dto, userId, uriBuilder )),HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<GlobalResponse<List<InvitationsInfo>>> getInvitations(
            @PathVariable String companyId
    ) {
    return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), invitationsService.getInvitations(companyId)));
    }

    @GetMapping("/verify")
    public ResponseEntity<GlobalResponse<InvitationsInfo>> verifyToken(
            @PathVariable String companyId,
            @RequestParam("token") String token,
            @RequestParam("timestamp") Long timestamp
    ) {
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), invitationsService.acceptInvitation(companyId,token,timestamp)));
    }

    @PutMapping("/{invitationId}")
    public ResponseEntity<GlobalResponse<InvitationsInfo>> refreshInvitations(
            @PathVariable @UUID String companyId,
            @PathVariable @UUID String invitationId,
            @RequestParam(value = "extend", required = false, defaultValue = "1") int days
    ) {
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), invitationsService.refreshInvitation(companyId, invitationId, days)));
    }

    @DeleteMapping("/")
    public ResponseEntity<GlobalResponse<Boolean>> revokeInvitation(
            @PathVariable @UUID String companyId,
            @RequestParam(value = "email") @Valid @Email String email
    ) {
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), invitationsService.revokeInvitation(companyId, email)));
    }

}
