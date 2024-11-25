package org.projectmanagement.presentation.config;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class PermissionEvaluator {

    // Check if the user has the required permission on the workspace
    public boolean hasPermissionOnWorkspaceOrProject(
            Authentication authentication
            , UUID objectId
            , List<String> requiredPermissions
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false; // User is not authenticated
        }

        String permissionAll = requiredPermissions.get(0);
        String permissionOne = requiredPermissions.get(1) + "_" + objectId;
        System.out.println("Checking permission: " + permissionAll + " or " + permissionOne);

        // Assuming your user has roles or permissions stored in their authorities
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(permissionAll) || authority.getAuthority().equals(permissionOne));
    }

//    public boolean hasPermissionOnProject(
//            Authentication authentication
//            , UUID projectId
//            , List<String> requiredPermissions
//    ) {
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return false; // User is not authenticated
//        }
//
//        String permissionAll = requiredPermissions.get(0);
//        String permissionOne = requiredPermissions.get(1) + "_" + projectId;
//        System.out.println("Checking permission: " + permissionAll + " or " + permissionOne);
//
//        // Assuming your user has roles or permissions stored in their authorities
//        return authentication.getAuthorities().stream()
//                .anyMatch(authority -> authority.getAuthority().equals(permissionAll) || authority.getAuthority().equals(permissionOne));
//    }

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