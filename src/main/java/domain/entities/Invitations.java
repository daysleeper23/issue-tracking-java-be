package domain.entities;

import domain.exceptions.InvalidInputException;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
public class Invitations {

    @Setter(AccessLevel.NONE)
    private UUID id;

    private String url;

    private String userEmail;

    private UUID companyId;

    private UUID workspaceId;

    private UUID roleId;

    private UUID invitedBy;

    @Setter(AccessLevel.NONE)
    private LocalDateTime expiredAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public void setExpiredAt(int day) {
        if (day < 0) {
            throw new InvalidInputException("Refresh day cannot be negative");
        }
        this.expiredAt = LocalDateTime.now().plusDays(day);
    }

}
