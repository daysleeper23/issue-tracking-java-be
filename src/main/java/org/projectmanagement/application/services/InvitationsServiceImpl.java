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

import java.security.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class InvitationsServiceImpl implements InvitationsService {

    private final InvitationsRepository invitationsRepository;

    private final WorkspacesService workspacesService;

    private final RolesService rolesService;

    private final EmailsService emailsService;

    private final CompaniesService companiesService;


    @Override
    @Transactional
    public Invitations sendInvitation(String companyId, InvitationsCreate dto, String loginId) {
        /*
          Todo: Implement the following checks
           + Check if the company & role & workspace exists
           + Check if the user is authorized to send the invitation
           + Check if the user is already existed and is not in any company
         */
        String workspaceName = null;
        String roleName = null;
        Companies company = companiesService.getCompany(companyId);
        if (invitationsRepository.findByEmail(dto.userEmail()) != null) {
            throw new ApplicationException(AppMessage.INVITATION_ALREADY_SENT);
        }
        if (!dto.isAdmin()) {
            WorkspacesRead workspace = workspacesService.findById(UUID.fromString(dto.workspaceId()))
                    .orElseThrow(() -> new ApplicationException(AppMessage.WORKSPACE_NOT_FOUND));
            Roles rp = rolesService.findById(UUID.fromString(dto.roleId()))
                    .orElseThrow(()->new ApplicationException(AppMessage.ROLE_NOT_FOUND));
            workspaceName = workspace.getName();
            roleName = rp.getName();
        }
        Invitations newInvitations = invitationsRepository.save(InvitationsMapper.mapper.dtoToEntity(dto));
        emailsService.sendInvitationEmail(dto.userEmail(),
                new InvitationMailBody(company.getName(),
                        newInvitations.getId().toString() ,
                        workspaceName, roleName,
                        dto.isAdmin(),
                        newInvitations.getCreatedAt().toEpochMilli()
                        ));
        return newInvitations;
    }

    @Override
    public List<InvitationsInfo> getInvitations(String companyId) {
        return InvitationsMapper.mapper
                .entitiesToInvitationInfos(invitationsRepository.getAll(UUID.fromString(companyId)));
    }

    @Override
    public boolean acceptInvitation(String token) {
        //Implement the logic to accept the invitation smtp email service
        return false;
    }

    @Override
    public Invitations refreshInvitation(String companyId, String invitationId, int days) {
        Invitations invitations = invitationsRepository.findById(invitationId);
        if (invitations == null || !invitations.getCompanyId().toString().equals(companyId)) {
            throw new ApplicationException(AppMessage.INVITATION_NOT_FOUND);
        }
        if (invitations.getExpiredAt().isAfter(Instant.now())){
            throw new ApplicationException(AppMessage.INVITATION_STILL_VALID);
        }
        return invitationsRepository.save(invitations.toBuilder().expiredAt(Instant.now().plus(days, ChronoUnit.DAYS)).build());
    }

    @Override
    public boolean revokeInvitation(String companyId, String invitationId) {
        Invitations invitations = invitationsRepository.findById(invitationId);
        if ( invitations == null|| !invitations.getCompanyId().toString().equals(companyId)) {
            throw new ApplicationException(AppMessage.INVITATION_NOT_FOUND);
        }
        return invitationsRepository.removeInvitation(invitations.getId());
    }
}
