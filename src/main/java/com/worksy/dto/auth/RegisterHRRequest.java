package com.worksy.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterHRRequest {

    @NotBlank
    @Size(max = 100)
    private String fullName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 6)
    private String password;

    private String phone;
    private String designation;

    @NotBlank(message = "Company name is required")
    private String companyName;
}
