package org.projectmanagement.presentation.config;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PermissionEvaluator {

    // Example: Replace this with real permission-checking logic
    public boolean hasPermissionOnWorkspace(
            Authentication authentication
            , UUID workspaceId
            , String requiredPermission
    ) {
        String combinedPermission = requiredPermission + "_" + workspaceId;
        authentication.getAuthorities().forEach(authority -> System.out.println("Authority: " + authority.getAuthority()));
        System.out.println("Checking permission: " + combinedPermission);
        if (!authentication.isAuthenticated()) {
            return false; // User is not authenticated
        }

        // Assuming your user has roles or permissions stored in their authorities
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(combinedPermission));
    }

//    public boolean hasPermission(Authentication authentication, String requiredPermission) {
//        authentication.getAuthorities().forEach(authority -> System.out.println("Authority: " + authority.getAuthority()));
//        System.out.println("Checking permission: " + requiredPermission);
//
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return false; // User is not authenticated
//        }
//
//        // Assuming your user has roles or permissions stored in their authorities
//        return authentication.getAuthorities().stream()
//                .anyMatch(authority -> authority.getAuthority().equals(requiredPermission));
//    }
}