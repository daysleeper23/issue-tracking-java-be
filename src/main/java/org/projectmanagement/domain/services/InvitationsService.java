package org.projectmanagement.domain.services;

import org.projectmanagement.application.dto.invitations.InvitationsCreate;
import org.projectmanagement.application.dto.invitations.InvitationsInfo;
import org.springframework.web.util.UriBuilder;

import java.util.List;

public interface InvitationsService {

    InvitationsInfo sendInvitation(String companyId, InvitationsCreate invitationsCreate, String loginId, UriBuilder uriBuilder);

    List<InvitationsInfo> getInvitations(String companyId);

    InvitationsInfo acceptInvitation(String companyId, String token, Long timestamp);

    InvitationsInfo refreshInvitation(String companyId, String invitationId, int days);

    boolean revokeInvitation(String companyId, String email);
}
