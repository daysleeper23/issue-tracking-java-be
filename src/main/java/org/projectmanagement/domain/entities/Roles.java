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

@Getter
@Setter
@NonNull
@AllArgsConstructor
public class Roles {
    @Id
    private final UUID id;

    private String name;

    private UUID companyId;

//    @CreatedDate
    private final Instant createdAt;

//    @LastModifiedDate
    private Instant updatedAt;
}
