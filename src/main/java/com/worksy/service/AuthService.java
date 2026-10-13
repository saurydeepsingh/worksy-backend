package com.worksy.service;

import com.worksy.dto.auth.*;
import com.worksy.entity.*;
import com.worksy.entity.enums.RoleName;
import com.worksy.exception.DuplicateResourceException;
import com.worksy.exception.InvalidCredentialsException;
import com.worksy.exception.ResourceNotFoundException;
import com.worksy.repository.*;
import com.worksy.security.JwtTokenProvider;
import com.worksy.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final HRProfileRepository hrProfileRepository;
    private final CompanyRepository companyRepository;
    private final RoleRepository roleRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;

    private boolean emailTaken(String email) {
        return userRepository.existsByEmail(email) || hrProfileRepository.existsByEmail(email);
    }

    @Transactional
    public AuthResponse registerUser(RegisterUserRequest request) {
        if (emailTaken(request.getEmail())) {
            throw new DuplicateResourceException("An account with this email already exists.");
        }

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new ResourceNotFoundException("ROLE_USER not found. Ensure roles are seeded."));

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .enabled(true)
                .emailVerified(false)
                .roles(roles)
                .build();

        user = userRepository.save(user);
        emailService.sendRegistrationEmail(user.getEmail(), user.getFullName());

        return buildAuthResponse(user.getEmail(), request.getPassword());
    }

    @Transactional
    public AuthResponse registerHR(RegisterHRRequest request) {
        if (emailTaken(request.getEmail())) {
            throw new DuplicateResourceException("An account with this email already exists.");
        }

        Role hrRole = roleRepository.findByName(RoleName.ROLE_HR)
                .orElseThrow(() -> new ResourceNotFoundException("ROLE_HR not found. Ensure roles are seeded."));

        // Reuse an existing company by name if present, otherwise create a new (unapproved) one.
        Company company = companyRepository.findAll().stream()
                .filter(c -> c.getName().equalsIgnoreCase(request.getCompanyName()))
                .findFirst()
                .orElseGet(() -> companyRepository.save(
                        Company.builder()
                                .name(request.getCompanyName())
                                .approved(false)
                                .build()));

        Set<Role> roles = new HashSet<>();
        roles.add(hrRole);

        HRProfile hr = HRProfile.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .designation(request.getDesignation())
                .company(company)
                .approved(false) // Admin must approve HR accounts before they can post jobs
                .enabled(true)
                .roles(roles)
                .build();

        hr = hrProfileRepository.save(hr);
        emailService.sendRegistrationEmail(hr.getEmail(), hr.getFullName());

        // HR accounts are pending admin approval; still issue a token so they can view their
        // pending-approval dashboard, but write access is blocked at the service layer until approved.
        return buildAuthResponse(hr.getEmail(), request.getPassword());
    }

    public AuthResponse login(LoginRequest request) {
        return buildAuthResponse(request.getEmail(), request.getPassword());
    }

    private AuthResponse buildAuthResponse(String email, String rawPassword) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, rawPassword));
        } catch (Exception ex) {
            throw new InvalidCredentialsException("Invalid email or password.");
        }

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        String token = jwtTokenProvider.generateToken(principal);
        List<String> roles = principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        return AuthResponse.builder()
                .token(token)
                .id(principal.getId())
                .fullName(principal.getFullName())
                .email(principal.getEmail())
                .type(principal.getType())
                .roles(roles)
                .build();
    }

    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        boolean exists = userRepository.existsByEmail(request.getEmail())
                || hrProfileRepository.existsByEmail(request.getEmail());
        if (!exists) {
            // Do not reveal whether the email exists - respond the same way either way.
            return;
        }

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .userEmail(request.getEmail())
                .expiryDate(LocalDateTime.now().plusMinutes(30))
                .used(false)
                .build();
        passwordResetTokenRepository.save(resetToken);

        String resetLink = "http://localhost:5173/reset-password?token=" + token;
        emailService.sendPasswordResetEmail(request.getEmail(), resetLink);
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid or expired reset token."));

        if (resetToken.isUsed() || resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new InvalidCredentialsException("Reset token has expired or already been used.");
        }

        String encoded = passwordEncoder.encode(request.getNewPassword());

        userRepository.findByEmail(resetToken.getUserEmail()).ifPresentOrElse(
                user -> {
                    user.setPassword(encoded);
                    userRepository.save(user);
                },
                () -> hrProfileRepository.findByEmail(resetToken.getUserEmail()).ifPresent(hr -> {
                    hr.setPassword(encoded);
                    hrProfileRepository.save(hr);
                })
        );

        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);
    }
}
