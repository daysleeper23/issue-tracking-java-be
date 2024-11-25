package org.projectmanagement.presentation.config;

import lombok.extern.log4j.Log4j2;
import org.projectmanagement.domain.entities.Permissions;
import org.projectmanagement.domain.repository.PermissionsRepoJpa;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Log4j2
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializePermissions(PermissionsRepoJpa permissionsRepository) {
        return args -> {
            // Define the list of permissions
            List<String> permissions = Arrays.asList(
                    "READONLY",
                    "WORKSPACE_CREATE",
                    "WORKSPACE_READ_ALL", "WORKSPACE_READ_ONE",
                    "WORKSPACE_UPDATE_ALL", "WORKSPACE_UPDATE_ONE",
                    "WORKSPACE_DELETE_ALL", "WORKSPACE_DELETE_ONE",
                    "PROJECT_CREATE",
                    "PROJECT_READ_ALL", "PROJECT_READ_ONE",
                    "PROJECT_UPDATE_ALL", "PROJECT_UPDATE_ONE",
                    "PROJECT_DELETE_ALL", "PROJECT_DELETE_ONE",
                    "COMPANY_UPDATE",
                    "COMPANY_READ",
                    "ROLE_CREATE",
                    "ROLE_READ_ALL",
                    "ROLE_DELETE_ALL",
                    "ROLE_UPDATE_ALL"
            );

            // Iterate over the permissions and insert if not exists
            permissions.forEach(permissionName -> {
                if (permissionsRepository.findByName(permissionName).isEmpty()) {
                    permissionsRepository.save(Permissions.builder().name(permissionName).build());
                    log.info("Permission {} created", permissionName);
                }
            });
        };
    }

}
