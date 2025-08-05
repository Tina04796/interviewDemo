package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordRequest {

    @NotBlank(message = "舊密碼不能空白")
    private String oldPassword;

    @NotBlank(message = "新密碼不能空白")
    private String newPassword;
}
