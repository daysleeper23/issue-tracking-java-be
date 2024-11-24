package org.projectmanagement.domain.services;

import org.projectmanagement.application.dto.invitations.InvitationsCreate;
import org.projectmanagement.application.dto.invitations.InvitationsInfo;
import org.projectmanagement.domain.entities.Invitations;

import java.util.List;

public interface InvitationsService {

    Invitations sendInvitation(String companyId, InvitationsCreate invitationsCreate, String loginId);

    List<InvitationsInfo> getInvitations(String companyId);

    boolean acceptInvitation(String token);

    Invitations refreshInvitation(String companyId, String invitationId, int days);

    boolean revokeInvitation(String companyId, String invitationId);
}
