package org.projectmanagement.domain.repository;

import org.projectmanagement.domain.entities.RolesPermissions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RolesPermissionsJpaRepo extends JpaRepository<RolesPermissions, UUID> {
    @Query(value = "SELECT permission_id " +
            "FROM roles_permissions rm " +
            "WHERE rm.role_id = :roleId ",
            nativeQuery = true)
    List<UUID> findAllPermissionsOfRoleByRoleId(@Param("roleId")  UUID roleId);

    @Query(value = "SELECT * " +
            "FROM roles_permissions rm " +
            "WHERE rm.role_id = :roleId ",
            nativeQuery = true)
    List<RolesPermissions> findAllRolePermissionsByRoleId(@Param("roleId") UUID roleId);

    @Query(value = "SELECT rp.* " +
            "FROM roles_permissions rp " +
            "JOIN roles r ON rp.role_id = r.id " +
            "WHERE r.company_id = :companyId",
            nativeQuery = true)
    List<RolesPermissions> findAllByCompanyId(@Param("companyId") UUID companyId);

}
