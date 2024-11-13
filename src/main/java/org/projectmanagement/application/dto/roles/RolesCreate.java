package org.projectmanagement.application.dto.roles;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RolesCreate {

    @NotBlank(message = "cannot be blank")
    private String name;

    @NotNull(message = "cannot be null")
    private UUID companyId;
}