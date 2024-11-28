package org.projectmanagement.application.dto.tasks;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.projectmanagement.application.utils.validation.annotation.EnumValidation;
import org.projectmanagement.application.utils.validation.annotation.NullOrNotBlank;
import org.projectmanagement.domain.enums.DefaultStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TasksUpdate {
        @NullOrNotBlank
        String name;
        String description;
        @Min(value = 0, message = "Priority must be greater than 0")
        @Max(value = 4, message = "Priority must be lesser than 4")
        Short priority;
        @EnumValidation(target = DefaultStatus.class)
        String status;
        String assigneeId;
        Instant startedAt;
        Instant endedAt;
}
