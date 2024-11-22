package org.projectmanagement.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.catalina.User;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "companies")
@SuperBuilder
@ToString
@NoArgsConstructor
public class Companies extends BaseEntity {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy=GenerationType.AUTO)
    private UUID id;

    private String name;

    private String description;

    @Column(name = "owner_id")
    private UUID ownerId;


    public Companies( UUID id, String name, String description, UUID ownerId, Instant createdAt, Instant updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.name = name;
        this.description = description;
        this.ownerId = ownerId;
    }

    public Companies(UUID id, String name, String description, UUID ownerId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ownerId = ownerId;
    }

    public Companies(BaseEntityBuilder<?, ?> b, UUID id, String name, String description, UUID ownerId) {
        super(b);
        this.id = id;
        this.name = name;
        this.description = description;
        this.ownerId = ownerId;
    }
}
