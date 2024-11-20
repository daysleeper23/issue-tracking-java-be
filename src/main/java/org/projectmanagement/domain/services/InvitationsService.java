package org.projectmanagement.domain.services;

import org.projectmanagement.application.dto.invitations.Invitations;
import org.projectmanagement.application.dto.invitations.InvitationsInfo;

import java.util.List;

public interface InvitationsService {

    boolean sendInvitation(String companyId, Invitations invitations, String loginId);

    List<InvitationsInfo> getInvitations(String companyId);

    boolean acceptInvitation(String token);

    boolean refreshInvitation(String companyId,String invitationId, int days);

    boolean revokeInvitation(String companyId, String invitationId);
}
