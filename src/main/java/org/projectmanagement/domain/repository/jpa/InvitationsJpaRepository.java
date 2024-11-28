package org.projectmanagement.domain.repository.jpa;

import org.projectmanagement.domain.entities.Invitations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvitationsJpaRepository extends JpaRepository<Invitations, UUID> {

    @Query(value = "SELECT * FROM invitations t " +
            "WHERE t.user_email =:email AND t.company_id=:companyId AND t.is_del=false",
            nativeQuery = true)
    Optional<Invitations> findByEmailAndCompanyId(@Param("email")String email,@Param("companyId") UUID companyId);

    @Query(value ="SELECT * FROM invitations t WHERE t.company_id =:companyId AND t.is_del=false" ,
            nativeQuery = true)
    List<Invitations> findByCompanyId(@Param("companyId") UUID companyId);

    @Modifying
    @Query(value ="UPDATE invitations SET is_del=true WHERE id =:id",nativeQuery = true)
    int removeInvitation(@Param("id")UUID id);

    @Query(value = "SELECT * FROM invitations t WHERE t.user_email =:id " +
            "AND t.company_id =:companyId " +
            "AND t.is_del = false",
            nativeQuery = true)
    Optional<Invitations> findByIdAndCompanyId(@Param("id")String id,@Param("companyId")UUID companyId);

}
