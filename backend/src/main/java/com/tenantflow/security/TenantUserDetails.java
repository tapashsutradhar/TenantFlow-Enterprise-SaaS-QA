package com.tenantflow.security;

import java.util.Collection;
import java.util.UUID;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public record TenantUserDetails(UUID id, UUID tenantId, String username, String password,
        Collection<? extends GrantedAuthority> authorities) implements UserDetails {
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    @Override public String getPassword() { return password; }
    @Override public String getUsername() { return username; }
}
