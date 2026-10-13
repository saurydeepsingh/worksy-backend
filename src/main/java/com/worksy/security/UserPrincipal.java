package com.worksy.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Common Spring Security principal used for User, HR and Admin logins.
 * "type" distinguishes which underlying table the id belongs to (USER / HR / ADMIN).
 */
@Getter
@Builder
@AllArgsConstructor
public class UserPrincipal implements UserDetails {

    private Long id;
    private String email;

    @JsonIgnore
    private String password;

    private String fullName;
    private String type; // USER, HR, ADMIN
    private boolean enabled;
    private Collection<? extends GrantedAuthority> authorities;

    public static UserPrincipal create(Long id, String email, String password, String fullName,
                                        String type, boolean enabled, List<String> roleNames) {
        List<GrantedAuthority> authorities = roleNames.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
        return new UserPrincipal(id, email, password, fullName, type, enabled, authorities);
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
        return enabled;
    }
}
