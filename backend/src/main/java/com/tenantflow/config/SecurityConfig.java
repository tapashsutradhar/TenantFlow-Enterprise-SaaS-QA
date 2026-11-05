package com.tenantflow.config;

import com.tenantflow.security.TenantUserDetails;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class SecurityConfig {
    private static final UUID NORTHWIND = UUID.fromString("c9c908c2-12ce-48d5-bec3-4f808f3d2c11");
    private static final UUID CONTOSO = UUID.fromString("d1a7d676-d651-46a2-9044-32a70b1e9ae3");

    @Bean PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable()).cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth.requestMatchers("/actuator/health", "/actuator/info").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll().anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults()).build();
    }

    @Bean CorsConfigurationSource corsConfigurationSource() {
        var config = new CorsConfiguration();
        config.setAllowedOrigins(java.util.List.of("http://localhost:5173", "http://localhost:4173"));
        config.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(java.util.List.of("Authorization", "Content-Type"));
        var source = new UrlBasedCorsConfigurationSource(); source.registerCorsConfiguration("/**", config); return source;
    }

    @Bean CommandLineRunner seedDemoUsers(JdbcTemplate jdbc, PasswordEncoder encoder,
            @Value("${tenantflow.demo.northwind-password}") String northwindPassword,
            @Value("${tenantflow.demo.contoso-password}") String contosoPassword) {
        return args -> {
            jdbc.update("INSERT INTO tenant(id,slug,display_name) VALUES (?,?,?) ON CONFLICT (id) DO NOTHING", NORTHWIND, "northwind", "Northwind Labs");
            jdbc.update("INSERT INTO tenant(id,slug,display_name) VALUES (?,?,?) ON CONFLICT (id) DO NOTHING", CONTOSO, "contoso", "Contoso Research");
            jdbc.update("INSERT INTO app_user(id,email,full_name,password_hash,role,tenant_id) VALUES (?,?,?,?,?,?) ON CONFLICT (email) DO NOTHING",
                    UUID.fromString("7ae4de92-3e21-4ec1-9175-e20d4dc87122"), "admin@northwind.test", "Avery Morgan", encoder.encode(northwindPassword), "ORG_ADMIN", NORTHWIND);
            jdbc.update("INSERT INTO app_user(id,email,full_name,password_hash,role,tenant_id) VALUES (?,?,?,?,?,?) ON CONFLICT (email) DO NOTHING",
                    UUID.fromString("81ccacbe-ae6b-481b-87b9-4915b4d572ec"), "admin@contoso.test", "Jordan Lee", encoder.encode(contosoPassword), "ORG_ADMIN", CONTOSO);
        };
    }

    public static UUID tenantId(Authentication authentication) {
        return ((TenantUserDetails) authentication.getPrincipal()).tenantId();
    }
}
