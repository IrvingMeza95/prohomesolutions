package com.EF2.prohomesolutions.models;

import com.EF2.prohomesolutions.enums.Country;
import com.EF2.prohomesolutions.enums.Role;
import com.EF2.prohomesolutions.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class User implements UserDetails {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    protected String id;
    @Column(nullable = false, unique = true)
    protected String email;
    @Column(nullable = false)
    protected String password;
    @Column(nullable = false)
    protected String name;
    protected String alias;
    @Column(nullable = false)
    protected String tel;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    protected Role role;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    protected UserType userType;
    @Column(nullable = false)
    protected Boolean enable;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    protected Country country;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(getId(), user.getId()) && Objects.equals(getEmail(), user.getEmail()) && getRole() == user.getRole() && getUserType() == user.getUserType() && Objects.equals(getEnable(), user.getEnable());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEmail());
    }
}
