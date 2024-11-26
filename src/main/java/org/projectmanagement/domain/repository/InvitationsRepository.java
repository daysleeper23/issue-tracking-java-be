package org.projectmanagement.domain.repository;

import org.projectmanagement.domain.entities.Invitations;

import java.util.List;
import java.util.UUID;

public interface InvitationsRepository {

    Invitations save(Invitations invitations);

    List<Invitations> getAll(UUID companyId);

    Invitations findById(String invitationId);

    Invitations findByIdAndCompanyId(String invitationId, String companyId);

    boolean removeInvitation(UUID invitationId);

    Invitations findByEmailAndCompanyId(String email, UUID companyId);
}
