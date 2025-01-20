package org.projectmanagement.application.dto.users;

import jakarta.validation.constraints.NotBlank;
import org.projectmanagement.application.dto.workspaces.WorkspacesRead;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class UsersAuth implements UserDetails {

    @NotBlank(message = "cannot be blank")
    private UUID id;

    @NotBlank(message = "cannot be blank")
    private String email;

    @NotBlank(message = "cannot be blank")
    private String name;

    @NotBlank(message = "cannot be blank")
    private String token;

    private UUID companyId;

    private List<UUID> workspaces;

    private UUID defaultWorkspace;

//    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return email;
    }
}
