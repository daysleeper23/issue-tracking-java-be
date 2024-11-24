package org.projectmanagement.presentation.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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

import java.util.List;

@RestController
@RequestMapping("/{companyId}/invitations")
@RequiredArgsConstructor
public class InvitationsController {

    private final InvitationsService invitationsService;

    @PostMapping("/")
    public ResponseEntity<GlobalResponse<Invitations>> sendInvitation(
            @PathVariable String companyId,
            @RequestBody @Valid InvitationsCreate dto
    ) {
        String userId = java.util.UUID.randomUUID().toString();
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), invitationsService.sendInvitation(companyId, dto, userId)));
    }

    @GetMapping("/")
    public ResponseEntity<GlobalResponse<List<InvitationsInfo>>> getInvitations(
            @PathVariable String companyId
    ) {
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), invitationsService.getInvitations(companyId)));
    }

    @GetMapping("/verify")
    public ResponseEntity<GlobalResponse<Boolean>> acceptInvitation(
            @RequestParam("token") String token
    ) {
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), invitationsService.acceptInvitation(token)));
    }

    @PutMapping("/{invitationId}")
    public ResponseEntity<GlobalResponse<Invitations>> refreshInvitations(
            @PathVariable @UUID String companyId,
            @PathVariable @UUID String invitationId,
            @RequestParam(value = "extend", required = false, defaultValue = "1") int days
    ) {
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), invitationsService.refreshInvitation(companyId, invitationId, days)));
    }

    @DeleteMapping("/{invitationId}")
    public ResponseEntity<GlobalResponse<Boolean>> revokeInvitation(
            @PathVariable @UUID String companyId,
            @PathVariable @UUID String invitationId
    ) {
        return ResponseEntity.ok(new GlobalResponse<>(HttpStatus.OK.value(), invitationsService.revokeInvitation(companyId, invitationId)));
    }

}
