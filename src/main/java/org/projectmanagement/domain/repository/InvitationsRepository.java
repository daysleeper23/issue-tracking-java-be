package org.projectmanagement.domain.repository;

import org.projectmanagement.domain.entities.Invitations;

import java.util.List;
import java.util.UUID;

public interface InvitationsRepository {

    boolean save(Invitations invitations);

    List<Invitations> getInvitationsByCompanyId(UUID companyId);

    Invitations findOne(String invitationId);

    boolean removeInvitation(Invitations invitations);

    boolean findOneByEmail(String email);
}
