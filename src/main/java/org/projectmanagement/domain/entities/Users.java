package org.projectmanagement.domain.entities;
import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@ToString
@AllArgsConstructor
@Getter
@Setter
//@EntityListeners(AuditingEntityListener.class)
@Entity
public class Users {

  @Id
  @Column(name = "id", unique = true)
  private UUID id;

  private String name;

  private String email;

  private String passwordHash;

  private String title;

  private Boolean isActive;

  private UUID companyId;

  private Boolean isOwner;

  private Boolean isDeleted;

//  @CreatedDate
  private final Instant createdAt;

//  @LastModifiedDate
  private Instant updatedAt;
}