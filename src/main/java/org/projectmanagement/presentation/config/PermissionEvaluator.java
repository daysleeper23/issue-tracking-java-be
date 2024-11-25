package org.projectmanagement.presentation.config;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class PermissionEvaluator {

    // Check if the user has the required permission on the workspace
    public boolean hasPermissionOnSingleResource(
            Authentication authentication
            , UUID resourceId
            , List<String> requiredPermissions
    ) {
        System.out.println("Checking permission on workspace or project: " + resourceId);

        if (authentication == null || !authentication.isAuthenticated()) {
            return false; // User is not authenticated
        }

        String permissionAll = requiredPermissions.get(0);
        String permissionOne = requiredPermissions.get(1) + "_" + resourceId;
        System.out.println("Checking permission: " + permissionAll + " or " + permissionOne);

        // Assuming your user has roles or permissions stored in their authorities
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(permissionAll) || authority.getAuthority().equals(permissionOne));
    }
}