package org.projectmanagement.domain.repository;

import org.projectmanagement.domain.entities.Invitations;

import java.util.List;
import java.util.UUID;

public interface InvitationsRepository {

    Invitations save(Invitations invitations);

    List<Invitations> getAll(UUID companyId);

    Invitations findById(String invitationId);

    boolean removeInvitation(UUID invitationId);

    Invitations findByEmail(String email);
}
