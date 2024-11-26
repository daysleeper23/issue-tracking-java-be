package org.projectmanagement.domain.services;

import org.projectmanagement.application.dto.invitations.InvitationsCreate;
import org.projectmanagement.application.dto.invitations.InvitationsInfo;
import org.projectmanagement.domain.entities.Invitations;
import org.springframework.web.util.UriBuilder;

import java.util.List;

public interface InvitationsService {

    InvitationsInfo sendInvitation(String companyId, InvitationsCreate invitationsCreate, String loginId, UriBuilder uriBuilder);

    List<InvitationsInfo> getInvitations(String companyId);

    boolean acceptInvitation(String token, Long timestamp);

    Invitations refreshInvitation(String companyId, String invitationId, int days);

    boolean revokeInvitation(String companyId, String invitationId);
}
