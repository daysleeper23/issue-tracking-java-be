package org.projectmanagement.test_data_factories;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.projectmanagement.domain.entities.Invitations;
import org.projectmanagement.domain.repository.jpa.InvitationsJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.UUID;

@Component
public class InvitationsDataFactory {

    @Autowired
    private InvitationsJpaRepository invitationsRepoJpa;

    public InvitationDaoMock createInvitation(UUID companyId, String userEmail) {
        Invitations.InvitationsBuilder<?, ?> builder = Invitations.builder();
        builder.userEmail(userEmail);
        builder.companyId(companyId);
        builder.expiredAt(Instant.now().plus(1, ChronoUnit.DAYS));
        Invitations invitations = builder.build();
        invitations = invitationsRepoJpa.save(invitations);
        return new InvitationDaoMock(invitations.getId(), invitations.getCreatedAt().toEpochMilli(), invitations.getUserEmail());
    }

    public InvitationDaoMock createInvitationExpired(UUID companyId, String userEmail) {
        Invitations.InvitationsBuilder<?, ?> builder = Invitations.builder();
        builder.userEmail(userEmail);
        builder.companyId(companyId);
        builder.expiredAt(Instant.now().minus(1, ChronoUnit.DAYS));
        Invitations invitations = builder.build();
        invitations = invitationsRepoJpa.save(invitations);
        return new InvitationDaoMock(invitations.getId(), invitations.getCreatedAt().toEpochMilli(), invitations.getUserEmail());
    }

    public void deleteAll() {
        invitationsRepoJpa.deleteAll();
    }

    public record InvitationDaoMock(UUID invitationId, Long createAt, String userEmail){
    }
}
