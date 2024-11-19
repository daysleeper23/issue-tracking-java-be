package org.projectmanagement.domain.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@ToString
@Getter
@Setter
@SuperBuilder
public class CompanyManagers extends BaseEntity {
    private UUID id;

    @NonNull
    private UUID userId;

    @NonNull
    private UUID companyId;

    @NonNull
    private UUID roleId;

}
