package com.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

	@NotBlank(message = "帳號不能空白")
    private String username;

    @NotBlank(message = "密碼不能空白")
    private String password;

    @Email(message = "Email錯誤")
    private String email;

}
