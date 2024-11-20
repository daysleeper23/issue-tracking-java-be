package org.projectmanagement.application.dto.users;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class UsersLogin implements UserDetails {
    @NotBlank(message = "cannot be blank")
    private String email;

    @NotBlank(message = "cannot be blank")
    private String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getUsername() {
        return "";
    }
}
