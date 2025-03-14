package itmo.blps.elearningplatform.model.user;

import itmo.blps.elearningplatform.model.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "\"user\"")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
public class User extends BaseEntity implements UserDetails {

    @NotBlank
    @Size(max = 32)
    @Column(name = "username", nullable = false, unique = true, length = 32)
    private String username;

    @NotBlank
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull
    @Column(name = "role", length = 16, nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @NotNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled = false;

    @Override
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
}
