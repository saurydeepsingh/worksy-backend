package com.worksy.controller;

import com.worksy.dto.auth.*;
import com.worksy.dto.common.ApiResponse;
import com.worksy.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Registration, login and password reset for Users, HR and Admin")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register a new job seeker account")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> registerUser(@Valid @RequestBody RegisterUserRequest request) {
        AuthResponse response = authService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("User registered successfully", response));
    }
    @Operation(summary = "Register a new HR / recruiter account (pending admin approval)")
    @PostMapping("/hr/register")
    public ResponseEntity<ApiResponse<AuthResponse>> registerHR(@Valid @RequestBody RegisterHRRequest request) {
        AuthResponse response = authService.registerHR(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("HR account registered successfully. Awaiting admin approval.", response));
    }

    @Operation(summary = "Login for job seekers")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }

    @Operation(summary = "Login for HR / recruiters")
    @PostMapping("/hr/login")
    public ResponseEntity<ApiResponse<AuthResponse>> loginHR(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }

    @Operation(summary = "Login for Admin")
    @PostMapping("/admin/login")
    public ResponseEntity<ApiResponse<AuthResponse>> loginAdmin(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        if (response.getRoles() == null || !response.getRoles().contains("ROLE_ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("This account does not have admin privileges."));
        }
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }

    @Operation(summary = "Request a password reset email")
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request);
        return ResponseEntity.ok(ApiResponse.success(
                "If an account exists for this email, a password reset link has been sent.", null));
    }

    @Operation(summary = "Reset password using a valid reset token")
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok(ApiResponse.success("Password reset successfully", null));
    }
}
