package org.projectmanagement.application.services;

import lombok.RequiredArgsConstructor;
import org.projectmanagement.application.dto.invitations.Invitations;
import org.projectmanagement.application.dto.invitations.InvitationsInfo;
import org.projectmanagement.application.dto.invitations.InvitationsMapper;
import org.projectmanagement.application.exceptions.AppMessage;
import org.projectmanagement.application.exceptions.ApplicationException;
import org.projectmanagement.domain.repository.InvitationsRepository;
import org.projectmanagement.domain.services.InvitationsService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class InvitationsServiceImpl implements InvitationsService {

    private final InvitationsRepository invitationsRepository;


    @Override
    public boolean sendInvitation(String companyId, Invitations invitationsDTO, String loginId) {
        /*
          Todo: Implement the following checks
           + Check if the company & role & workspace exists
           + Check if the user is authorized to send the invitation
           + Check if the user is already existed and is not in any company
         */
        if (invitationsRepository.findOneByEmail(invitationsDTO.userEmail())) {
            throw new ApplicationException(AppMessage.INVITATION_ALREADY_SENT);
        }
        org.projectmanagement.domain.entities.Invitations invitations = InvitationsMapper.mapper.dtoToEntity(invitationsDTO);
        invitationsRepository.save(invitations.toBuilder()
                .companyId(UUID.fromString(companyId))
                .invitedBy(UUID.fromString(loginId))
                .build());
        return true;
    }

    @Override
    public List<InvitationsInfo> getInvitations(String companyId) {
        return InvitationsMapper.mapper
                .entitiesToInvitationInfos(invitationsRepository.getInvitationsByCompanyId(UUID.fromString(companyId)));
    }

    @Override
    public boolean acceptInvitation(String token) {
        //Implement the logic to accept the invitation smtp email service
        return false;
    }

    @Override
    public boolean refreshInvitation(String companyId, String invitationId, int days) {
        org.projectmanagement.domain.entities.Invitations invitations = invitationsRepository.findOne(invitationId);
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
        org.projectmanagement.domain.entities.Invitations invitations = invitationsRepository.findOne(invitationId);
        if ( invitations == null|| !invitations.getCompanyId().toString().equals(companyId)) {
            throw new ApplicationException(AppMessage.INVITATION_NOT_FOUND);
        }
        return invitationsRepository.removeInvitation(invitations);
    }
}
