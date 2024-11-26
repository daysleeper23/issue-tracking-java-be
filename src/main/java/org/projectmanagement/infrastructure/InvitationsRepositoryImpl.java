package org.projectmanagement.infrastructure;


import lombok.RequiredArgsConstructor;
import org.projectmanagement.domain.entities.Invitations;
import org.projectmanagement.domain.repository.InvitationsRepository;
import org.projectmanagement.domain.repository.jpa.InvitationsJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class InvitationsRepositoryImpl implements InvitationsRepository {

    private final InvitationsJpaRepository jpa;

    @Override
    public List<Invitations> getAll(UUID companyId) {
        return jpa.findByCompanyId(companyId);
    }

    @Override
    public Invitations findById(String invitationId) {
        return jpa.findById(UUID.fromString(invitationId)).orElse(null);
    }

    @Override
    public Invitations findByIdAndCompanyId(String invitationId, String companyId) {
        return jpa.findByIdAndCompanyId(UUID.fromString(invitationId), UUID.fromString(companyId)).orElse(null);
    }

    @Override
    public boolean removeInvitation(UUID invitationId) {
        return jpa.removeInvitation(invitationId) > 0;
    }

    @Override
    public Invitations findByEmailAndCompanyId(String email, UUID companyId) {
        return jpa.findByEmailAndCompanyId(email,companyId).orElse(null);
    }

    @Override
    public Invitations save(Invitations invitations) {
        return jpa.save(invitations);
    }
}
