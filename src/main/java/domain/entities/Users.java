package domain.entities;
import java.time.Instant;
import java.util.UUID;

import lombok.*;


@Builder
@ToString
public class Users {
  private UUID id;

  @NonNull
  @Setter
  @Getter
  private String name;

  @NonNull
  @Setter
  @Getter
  private String email;

  @NonNull
  @Setter
  private String passwordHash;

  @Getter
  @Setter
  private String title;

  @NonNull
  @Getter
  @Setter
  private Boolean isActive;

  @NonNull
  @Getter
  private UUID companyId;

  @NonNull
  @Getter
  private Boolean isOwner;

  @NonNull
  @Getter
  private Instant createdAt;

  @NonNull
  @Getter
  @Setter
  private Instant updatedAt;
}