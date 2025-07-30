package com.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequest {

	@NotBlank(message = "帳號不能空白")
	private String username;

	@NotBlank(message = "Email不能空白")
	@Email(message = "Email錯誤")
	private String email;

}
