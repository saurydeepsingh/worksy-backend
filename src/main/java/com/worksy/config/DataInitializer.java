package com.worksy.config;

import com.worksy.entity.Role;
import com.worksy.entity.User;
import com.worksy.entity.enums.RoleName;
import com.worksy.repository.RoleRepository;
import com.worksy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Seeds mandatory reference data on startup:
 *  - the three roles (ROLE_USER, ROLE_HR, ROLE_ADMIN)
 *  - a default Admin account (email/password below), only if one doesn't already exist.
 *
 * Runs only when app.data.seed=true (default). Sample HR/users/companies/jobs/DSA
 * questions are seeded separately by DemoDataInitializer (Stage 3).
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.data.seed:true}")
    private boolean seedEnabled;

    @Override
    public void run(String... args) {
        if (!seedEnabled) return;

        for (RoleName roleName : RoleName.values()) {
            roleRepository.findByName(roleName).orElseGet(() ->
                    roleRepository.save(Role.builder().name(roleName).build()));
        }
        log.info("Roles ensured: ROLE_USER, ROLE_HR, ROLE_ADMIN");

        String adminEmail = "admin@worksy.com";
        if (!userRepository.existsByEmail(adminEmail)) {
            Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN).orElseThrow();
            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);

            User admin = User.builder()
                    .fullName("Worksy Admin")
                    .email(adminEmail)
                    .password(passwordEncoder.encode("Admin@123"))
                    .enabled(true)
                    .emailVerified(true)
                    .roles(roles)
                    .build();
            userRepository.save(admin);
            log.info("Default admin account created: {} / Admin@123 (CHANGE THIS IN PRODUCTION)", adminEmail);
        }
    }
}
