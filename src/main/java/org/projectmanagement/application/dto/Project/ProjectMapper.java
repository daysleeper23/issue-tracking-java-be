package org.projectmanagement.application.dto.Project;

import org.projectmanagement.domain.entities.Projects;

import java.util.UUID;

public class ProjectMapper {

    public static Projects createDTOToProject(ProjectCreateDTO dto) {
        return Projects.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .endDate(dto.getEndDate())
                .startDate(dto.getStartDate())
                .priority(dto.getPriority())
                .status(dto.getStatus())
                .leaderId(dto.getLeaderId())
                .build();
    }

    public static Projects updateDTOToProject(UUID id, ProjectUpdateDTO dto) {
        return Projects.builder()
                .id(id)
                .description(dto.getDescription())
                .endDate(dto.getEndDate())
                .startDate(dto.getStartDate())
                .priority(dto.getPriority())
                .status(dto.getStatus())
                .build();
    }
}
