package org.projectmanagement.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
public class Workspaces {
    @Id
    private final UUID id;

    @NonNull
    private String name;

    private String description;

    @NonNull
    private UUID companyId;

//    @CreatedDate
    private final Instant createdAt;

//    @LastModifiedDate
    private Instant updatedAt;
}
