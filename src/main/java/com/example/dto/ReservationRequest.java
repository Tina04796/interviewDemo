package com.example.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequest {

	@NotNull(message = "房間ID不能空白")
	private Long userId;
	
	@NotNull(message = "預約名稱不能空白")
	private Long roomId;
	
	@NotNull(message = "開始時間不能空白")
	private LocalDateTime startTime;
	
	@NotNull(message = "結束時間不能空白")
	private LocalDateTime endTime;

}
