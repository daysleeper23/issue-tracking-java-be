package org.projectmanagement.application.services;

import lombok.RequiredArgsConstructor;
import org.projectmanagement.application.dto.invitations.InvitationsCreate;
import org.projectmanagement.application.dto.invitations.InvitationsInfo;
import org.projectmanagement.application.dto.invitations.InvitationsMapper;
import org.projectmanagement.application.dto.mail.InvitationMailBody;
import org.projectmanagement.application.dto.workspaces.WorkspacesRead;
import org.projectmanagement.application.exceptions.AppMessage;
import org.projectmanagement.application.exceptions.ApplicationException;
import org.projectmanagement.domain.entities.*;
import org.projectmanagement.domain.repository.InvitationsRepository;
import org.projectmanagement.domain.services.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriBuilder;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Service
public class InvitationsServiceImpl implements InvitationsService {

    private final InvitationsRepository invitationsRepository;

    private final WorkspacesService workspacesService;

    private final RolesService rolesService;

//    private final EmailsService emailsService;

    private final CompaniesService companiesService;


    @Override
    @Transactional
    public InvitationsInfo sendInvitation(String companyId, InvitationsCreate dto, String loginId, UriBuilder uriBuilder) {
        String workspaceName;
        String roleName;
        Companies company = companiesService.getCompany(companyId);
        if (invitationsRepository.findByEmailAndCompanyId(dto.userEmail(), company.getId()) != null) {
            throw new ApplicationException(AppMessage.INVITATION_ALREADY_SENT);
        }
        if (!dto.isAdmin()) {
            WorkspacesRead workspace = workspacesService.findById(UUID.fromString(dto.workspaceId()))
                    .orElseThrow(() -> new ApplicationException(AppMessage.WORKSPACE_NOT_FOUND));
            Roles rp = rolesService.findById(UUID.fromString(dto.roleId()))
                    .orElseThrow(()->new ApplicationException(AppMessage.ROLE_NOT_FOUND));
            workspaceName = workspace.getName();
            roleName = rp.getName();
        } else {
            roleName = null;
            workspaceName = null;
        }
        Invitations newInvitations = invitationsRepository.save(InvitationsMapper.mapper.dtoToEntity(dto).toBuilder()
                        .companyId(company.getId()).build());
//        CompletableFuture<Void> sendEmail = CompletableFuture.runAsync(() ->
//        emailsService.sendInvitationEmail(dto.userEmail(), uriBuilder,
//                new InvitationMailBody(company.getName(),
//                        newInvitations.getId().toString() ,
//                        workspaceName, roleName,
//                        dto.isAdmin(),
//                        newInvitations.getCreatedAt().toEpochMilli()
//                        )));
//        try {
//            sendEmail.get();
//        } catch (Exception e) {
//            throw new ApplicationException(e.getMessage());
//        }
        return InvitationsMapper.mapper.entityToInvitationInfo(newInvitations);
    }

    @Override
    public List<InvitationsInfo> getInvitations(String companyId) {
        return InvitationsMapper.mapper
                .entitiesToInvitationInfos(invitationsRepository.getAll(UUID.fromString(companyId)));
    }

    @Override
    public InvitationsInfo acceptInvitation(String companyId, String token, Long timestamp) {
        //Todo: Implement the following checks
        // + Check if the invitation is still valid
        // + Check if the user is already in a company
        Invitations invitation = invitationsRepository.findByIdAndCompanyId(token, companyId);
        if (invitation == null) {
            throw new ApplicationException(AppMessage.INVITATION_NOT_FOUND);
        }
        if (invitation.getCreatedAt().toEpochMilli() != timestamp) {
            throw new ApplicationException(AppMessage.INVITATION_NOT_FOUND);
        }
        if (invitation.getExpiredAt().isBefore(Instant.now())) {
            throw new ApplicationException(AppMessage.INVITATION_EXPIRED);
        }
        return InvitationsMapper.mapper.entityToInvitationInfo(invitation);
    }

    @Override
    public InvitationsInfo refreshInvitation(String companyId, String invitationId, int days) {
        Invitations invitations = invitationsRepository.findByIdAndCompanyId(invitationId,companyId);
        if (invitations == null || !invitations.getCompanyId().toString().equals(companyId)) {
            throw new ApplicationException(AppMessage.INVITATION_NOT_FOUND);
        }
        if (invitations.getExpiredAt().isAfter(Instant.now())){
            throw new ApplicationException(AppMessage.INVITATION_STILL_VALID);
        }
        return InvitationsMapper.mapper.entityToInvitationInfo(invitationsRepository
                .save(invitations.toBuilder().expiredAt(Instant.now().plus(days, ChronoUnit.DAYS)).build()));
    }

    @Transactional
    @Override
    public boolean revokeInvitation(String companyId, String email) {
        Invitations invitations = invitationsRepository.findByEmailAndCompanyId(email, UUID.fromString(companyId));
        if ( invitations == null|| !invitations.getCompanyId().toString().equals(companyId)) {
            throw new ApplicationException(AppMessage.INVITATION_NOT_FOUND);
        }
        return invitationsRepository.removeInvitation(invitations.getId());
    }
}
