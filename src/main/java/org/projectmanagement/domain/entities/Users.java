package org.projectmanagement.domain.entities;
import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.*;
import javax.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@ToString
@AllArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Users {
  @Id
  private UUID id;

  private String name;

  private String email;

  private String passwordHash;

  private String title;

  private Boolean isActive;

  private UUID companyId;

  private Boolean isOwner;

//  @CreatedDate
  private final Instant createdAt;

//  @LastModifiedDate
  private Instant updatedAt;
}