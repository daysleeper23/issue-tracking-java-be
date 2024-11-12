package org.projectmanagement.domain.entities;

import org.projectmanagement.domain.exceptions.InvalidInputException;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder(builderClassName = "InvitationsBuilder",toBuilder = true)
public class Invitations {

    @Setter(AccessLevel.NONE)
    private UUID id;

    private String url;

    @NonNull
    private String userEmail;

    @NonNull
    private UUID companyId;

    private UUID workspaceId;

    private UUID roleId;

    @NonNull
    private UUID invitedBy;

    private Instant expiredAt;
    @Builder.Default
    private Instant createdAt = Instant.now() ;
    @Builder.Default
    private Instant updatedAt = Instant.now();

    public static class InvitationsBuilder{

        public InvitationsBuilder refreshExpired(int day){
            if (day < 0) {
                throw new InvalidInputException("Refresh day cannot be negative");
            }
            this.expiredAt = Instant.now().plus(day, java.time.temporal.ChronoUnit.DAYS);
            return this;
        }

    }

}
