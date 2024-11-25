package org.projectmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//Todo: Remove the exclude attribute from the @SpringBootApplication annotation after setting up the database
@SpringBootApplication
@EnableJpaRepositories(basePackages = "org.projectmanagement.domain.repository")

public class ProjectManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectManagementApplication.class, args);
    }
}
