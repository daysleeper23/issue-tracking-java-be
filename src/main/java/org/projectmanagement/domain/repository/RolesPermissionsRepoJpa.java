package org.projectmanagement.domain.repository;

import org.projectmanagement.application.dto.roles_permissions.RolesPermissionsRead;
import org.projectmanagement.domain.entities.RolesPermissions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public interface RolesPermissionsRepoJpa extends JpaRepository<RolesPermissions, UUID> {
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

    @Query(value = "SELECT rp.id, rp.role_id, rp.permission_id, r.name " +
        "FROM roles_permissions rp " +
        "JOIN roles r ON rp.role_id = r.id " +
        "WHERE r.company_id = :companyId",
        nativeQuery = true)
    List<Map<String, Object>> findAllByCompanyIdRaw(@Param("companyId") UUID companyId);
    
    default List<RolesPermissionsRead> findAllByCompanyId(@Param("companyId") UUID companyId) {
        List<Map<String, Object>> rawResults = findAllByCompanyIdRaw(companyId);
        return rawResults.stream().map(
            row -> new RolesPermissionsRead(
                UUID.fromString(row.get("id").toString())
                , UUID.fromString(row.get("role_id").toString())
                , UUID.fromString(row.get("permission_id").toString())
                , row.get("name").toString()
            )).toList();
    }



}
