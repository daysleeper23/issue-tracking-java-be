package org.projectmanagement.application.dto.Project;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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

    @Min(0)
    @Max(4)
    private short priority;

    private DefaultStatus status;
}
