package com.tenantflow.directory;

import com.tenantflow.config.SecurityConfig;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class DirectoryController {
    private final JdbcTemplate jdbc;
    public DirectoryController(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    @GetMapping("/me")
    public Profile me(Authentication auth) {
        var tenantId = SecurityConfig.tenantId(auth);
        return jdbc.queryForObject("SELECT u.id,u.email,u.full_name,u.role,t.id tenant_id,t.slug,t.display_name FROM app_user u JOIN tenant t ON t.id=u.tenant_id WHERE u.id=? AND u.tenant_id=?",
                (rs, n) -> new Profile(rs.getObject("id", UUID.class),rs.getString("email"),rs.getString("full_name"),rs.getString("role"),rs.getObject("tenant_id",UUID.class),rs.getString("slug"),rs.getString("display_name")),
                ((com.tenantflow.security.TenantUserDetails) auth.getPrincipal()).id(), tenantId);
    }

    @GetMapping("/organization")
    public Organization organization(Authentication auth) {
        return jdbc.queryForObject("SELECT id,slug,display_name,created_at FROM tenant WHERE id=?",
                (rs,n)->new Organization(rs.getObject("id",UUID.class),rs.getString("slug"),rs.getString("display_name"),rs.getObject("created_at",OffsetDateTime.class)), SecurityConfig.tenantId(auth));
    }

    @GetMapping("/members")
    public List<Member> members(Authentication auth) {
        return jdbc.query("SELECT id,email,full_name,role,created_at FROM app_user WHERE tenant_id=? ORDER BY full_name",
                (rs,n)->new Member(rs.getObject("id",UUID.class),rs.getString("email"),rs.getString("full_name"),rs.getString("role"),rs.getObject("created_at",OffsetDateTime.class)), SecurityConfig.tenantId(auth));
    }

    @PostMapping("/members")
    @ResponseStatus(HttpStatus.CREATED)
    public Member addMember(@Valid @RequestBody AddMember body, Authentication auth) {
        if (!auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ORG_ADMIN"))) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        var id = UUID.randomUUID(); var tenant = SecurityConfig.tenantId(auth);
        try {
            jdbc.update("INSERT INTO app_user(id,email,full_name,password_hash,role,tenant_id) VALUES (?,?,?,?,?,?)", id, body.email().trim().toLowerCase(), body.fullName().trim(), "!invite-pending", body.role(), tenant);
        } catch (org.springframework.dao.DuplicateKeyException ex) { throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already in use"); }
        return jdbc.queryForObject("SELECT id,email,full_name,role,created_at FROM app_user WHERE id=? AND tenant_id=?",
                (rs,n)->new Member(rs.getObject("id",UUID.class),rs.getString("email"),rs.getString("full_name"),rs.getString("role"),rs.getObject("created_at",OffsetDateTime.class)), id, tenant);
    }

    public record Profile(UUID id,String email,String fullName,String role,UUID tenantId,String tenantSlug,String tenantName) {}
    public record Organization(UUID id,String slug,String displayName,OffsetDateTime createdAt) {}
    public record Member(UUID id,String email,String fullName,String role,OffsetDateTime createdAt) {}
    public record AddMember(@Email @NotBlank @Size(max=254) String email, @NotBlank @Size(max=160) String fullName,
                            @NotBlank @Pattern(regexp="ORG_ADMIN|MANAGER|EMPLOYEE") String role) {}
}
