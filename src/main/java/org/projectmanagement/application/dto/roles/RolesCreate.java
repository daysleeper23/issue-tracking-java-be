package org.projectmanagement.application.dto.roles;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RolesCreate {

    @NotBlank(message = "Name cannot be blank")
    private String name;
}
