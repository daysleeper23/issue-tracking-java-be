package org.projectmanagement.application.dto.Project;

import lombok.*;
import org.projectmanagement.domain.enums.DefaultStatus;

import java.time.Instant;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectUpdateDTO {
    private String name;

    private String description;

    private Instant endDate;

    private Instant startDate;

    private short priority;

    private DefaultStatus status;
}
