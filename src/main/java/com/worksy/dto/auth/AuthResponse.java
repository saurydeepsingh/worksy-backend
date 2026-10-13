package com.worksy.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private Long id;
    private String fullName;
    private String email;
    private String type; // USER, HR, ADMIN
    private List<String> roles;
}
