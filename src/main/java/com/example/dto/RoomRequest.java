package com.example.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomRequest {

	@NotBlank(message = "房間名稱不能空白")
	private String name;

	private String location;

	@Min(value = 1, message = "容量必須大於0")
	private Integer capacity;

}
