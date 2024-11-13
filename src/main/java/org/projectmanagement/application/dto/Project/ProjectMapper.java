package org.projectmanagement.application.dto.Project;

import org.projectmanagement.domain.entities.Projects;

import java.time.Instant;
import java.util.UUID;

public class ProjectMapper {

    public static Projects createDTOToProject(ProjectCreateDTO dto) {
        if (dto.getStartDate() != null && dto.getEndDate() != null) {
            validateDates(dto.getStartDate(), dto.getEndDate());
        }
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
        if (dto.getStartDate() != null && dto.getEndDate() != null) {
            validateDates(dto.getStartDate(), dto.getEndDate());
        }
        return Projects.builder()
                .id(id)
                .description(dto.getDescription())
                .endDate(dto.getEndDate())
                .startDate(dto.getStartDate())
                .priority(dto.getPriority())
                .status(dto.getStatus())
                .build();
    }

    public static void validateDates(Instant startDate, Instant endDate) {
        if(startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must not be after end date.");
        }
    }
}
