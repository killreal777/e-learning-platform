package itmo.blps.elearningplatform.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.beans.Transient;
import java.util.Collection;
import java.util.Random;
import java.util.Set;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
public class User implements UserDetails {

    @Min(1)
    @NotNull
    private Integer id;

    @NotBlank
    @Size(max = 32)
    private String username;

    @NotBlank
    private String password;

    @NotNull
    private Role role;

    @NotNull
    private Boolean enabled = false;

    @Override
    @Transient
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    @Transient
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @Transient
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @Transient
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public enum Role {
        ROLE_OWNER,
        ROLE_ADMIN,
        ROLE_TEACHER,
        ROLE_STUDENT
    }

    @Override
    public int hashCode() {
        if (id == null) {
            return super.hashCode();
        }
        return id.hashCode();
    }
}
