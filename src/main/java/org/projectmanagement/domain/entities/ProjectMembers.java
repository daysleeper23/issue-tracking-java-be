package org.projectmanagement.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "project_members")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
public class ProjectMembers extends BaseEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @NonNull
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @NonNull
    @Column(name = "project_id", nullable = false)
    private UUID projectId;

    @NonNull
    @Column(name = "subscribed", nullable = false)
    private Boolean subscribed;

}
