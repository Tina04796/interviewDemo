package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

	@NotBlank(message = "Username required")
    private String usernameOrEmail;

	@NotBlank(message = "Password required")
    private String password;

}
