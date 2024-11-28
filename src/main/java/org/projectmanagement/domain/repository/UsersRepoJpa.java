package org.projectmanagement.domain.repository;

import org.projectmanagement.domain.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersRepoJpa extends JpaRepository<Users, UUID> {

    @Query(value = "SELECT * " +
            "FROM users u " +
            "WHERE u.id = :id " +
            "AND u.company_id = :companyId " +
            "AND u.is_deleted = false"
        , nativeQuery = true
    )
    Optional<Users> findByIdForCompany(@Param("id") UUID id, @Param("companyId") UUID companyId);

    @Query(value = "SELECT * " +
            "FROM users u " +
            "WHERE u.company_id = :companyId " +
            "AND u.is_deleted = false"
        , nativeQuery = true)
    List<Users> findAllByCompanyId(@Param("companyId") UUID companyId);

    @Query(value = "SELECT * " +
            "FROM users u " +
            "WHERE u.email = :email " +
            "AND u.is_deleted = false"
        , nativeQuery = true)
    Optional<Users> findOneByEmail(@Param("email") String email);

    @Modifying
    @Query(value = "UPDATE users " +
            "SET is_deleted = true " +
            "WHERE id = :id"
        , nativeQuery = true
    )
    void deleteById(@Param("id") UUID id);
}
