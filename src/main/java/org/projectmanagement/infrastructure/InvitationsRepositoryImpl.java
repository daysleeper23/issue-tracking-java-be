package org.projectmanagement.infrastructure;


import lombok.RequiredArgsConstructor;
import org.projectmanagement.domain.entities.Invitations;
import org.projectmanagement.domain.entities.Tasks;
import org.projectmanagement.domain.repository.InvitationsRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Repository
public class InvitationsRepositoryImpl implements InvitationsRepository {

    private final InMemoryDatabase inMemoryDatabase;

    @Override
    public List<Invitations> getInvitationsByCompanyId(UUID companyId) {
        return inMemoryDatabase.getInvitations().stream().filter(i->i.getCompanyId().equals(companyId)).toList();
    }

    @Override
    public Invitations findOne(String invitationId) {
        return inMemoryDatabase.getInvitations().stream().filter(i->i.getId().equals(UUID.fromString(invitationId))).findFirst().orElse(null);
    }

    @Override
    public boolean removeInvitation(Invitations invitations) {
        return inMemoryDatabase.getInvitations().remove(invitations);
    }

    @Override
    public boolean findOneByEmail(String email) {
        return inMemoryDatabase.getInvitations().stream().anyMatch(i->i.getUserEmail().equals(email));
    }

    @Override
    public boolean save(Invitations invitations) {
        Invitations save;
        int index = IntStream.range(0, inMemoryDatabase.getInvitations().size())
                .filter(i -> inMemoryDatabase.getInvitations().get(i).getId() != null &&
                        inMemoryDatabase.getInvitations().get(i).getId().equals(invitations.getId())
                )
                .findFirst()
                .orElse(-1);
        if (index != -1) {
            save = inMemoryDatabase.getInvitations().set(index, invitations);
        } else {
            save = invitations.toBuilder().id(UUID.randomUUID()).build();
            inMemoryDatabase.getInvitations().add(save);
        }
        return true;
    }
}
