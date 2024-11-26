package org.projectmanagement.test_data_factories;

import org.projectmanagement.domain.repository.jpa.InvitationsJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InvitationsDataFactory {

    @Autowired
    private InvitationsJpaRepository invitationsRepoJpa;

    public void deleteAll() {
        invitationsRepoJpa.deleteAll();
    }

}
