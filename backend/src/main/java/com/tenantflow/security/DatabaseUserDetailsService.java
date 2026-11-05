package com.tenantflow.security;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {
    private final JdbcTemplate jdbc;
    public DatabaseUserDetailsService(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    @Override public UserDetails loadUserByUsername(String email) {
        var rows = jdbc.query("SELECT id, tenant_id, email, password_hash, role FROM app_user WHERE lower(email)=lower(?)",
                (rs, n) -> new TenantUserDetails(rs.getObject("id", java.util.UUID.class),
                        rs.getObject("tenant_id", java.util.UUID.class), rs.getString("email"),
                        rs.getString("password_hash"), List.of(new SimpleGrantedAuthority("ROLE_" + rs.getString("role")))), email);
        if (rows.isEmpty()) throw new UsernameNotFoundException("Account not found");
        return rows.getFirst();
    }
}
