package domain.entities;

import domain.enums.DefaultStatus;
import domain.exceptions.InvalidInputException;
import lombok.*;
import org.apache.commons.lang3.EnumUtils;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Data
public class Tasks {

    @Setter(AccessLevel.NONE)
    private UUID id;

    private String name;

    private String description;

    private DefaultStatus status;

    private UUID assigneeId;

    @Setter(AccessLevel.NONE)
    private short priority;

    private UUID projectId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Setter(AccessLevel.NONE)
    private LocalDateTime startedAt;

    @Setter(AccessLevel.NONE)
    private LocalDateTime endedAt;


    public void setPriority(short priority){
        if (priority < 0 || priority > 4){
            throw new InvalidInputException("Illegal priority value");
        }
        this.priority = priority;
    }


    public void setStatus(String status){
        if (!EnumUtils.isValidEnum(DefaultStatus.class,status)){
            throw new InvalidInputException("Illegal task status");
        }
        this.status = DefaultStatus.valueOf(status);
    }

    public void setStartedAt(LocalDateTime startedAt) {
        if (startedAt.isAfter(LocalDateTime.now())) {
            throw new InvalidInputException("Started at cannot be in the future");
        }
        this.startedAt = startedAt;
    }


    public void setEndedAt(LocalDateTime endedAt) {
        if (endedAt.isBefore(LocalDateTime.now())) {
            throw new InvalidInputException("Ended at cannot be in the past");
        }
        this.endedAt = endedAt;
    }

}
