package org.projectmanagement.domain.repository.jpa;

import org.projectmanagement.domain.entities.Invitations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvitationsJpaRepository extends JpaRepository<Invitations, UUID> {

    @Query("SELECT i FROM Invitations i WHERE i.userEmail =?1 and i.isDel=false")
    Optional<Invitations> findByEmail(String email);

    @Query("SELECT i FROM Invitations i WHERE i.companyId =?1 and i.isDel=false")
    List<Invitations> findByCompanyId(UUID companyId);

    @Modifying
    @Query("UPDATE Invitations i SET i.isDel=false WHERE i.id =?1" )
    boolean removeInvitation(UUID id);

}
