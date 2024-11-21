package org.projectmanagement.presentation.config;

import org.projectmanagement.domain.entities.Permissions;
import org.projectmanagement.domain.repository.PermissionsJpaRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializePermissions(PermissionsJpaRepo permissionsRepository) {
        return args -> {
            // Define the list of permissions
            List<String> permissions = Arrays.asList(
                    "READONLY",
                    "WORKSPACE_CREATE",
                    "WORKSPACE_UPDATE_ALL",
                    "WORKSPACE_READ_ALL",
                    "WORKSPACE_DELETE_ALL",
                    "PROJECT_CREATE",
                    "PROJECT_UPDATE_ALL",
                    "PROJECT_READ_ALL",
                    "PROJECT_DELETE_ALL",
                    "WORKSPACE_UPDATE_SINGLE",
                    "PROJECT_UPDATE_SINGLE",
                    "COMPANY_UPDATE",
                    "ROLE_CREATE",
                    "ROLE_READ_ALL",
                    "ROLE_DELETE_ALL",
                    "ROLE_UPDATE_ALL"
            );

            // Iterate over the permissions and insert if not exists
            permissions.forEach(permissionName -> {
                if (permissionsRepository.findByName(permissionName).isEmpty()) {
                    permissionsRepository.save(Permissions.builder().name(permissionName).build());
                    System.out.println("Inserted permission: " + permissionName);
                }
            });
        };
    }
}
