package org.projectmanagement.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.projectmanagement.domain.enums.DefaultStatus;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
public class Projects extends BaseEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @NonNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "end_date")
    private Instant endDate;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "priority", nullable = false)
    private int priority;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DefaultStatus status;

    @NonNull
    @JoinColumn(name = "leader_id", nullable = false)
    private UUID leaderId;

    @NonNull
    @Column(name = "workspace_id", nullable = false)
    private UUID workspaceId;
}
