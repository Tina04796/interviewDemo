package com.example.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationSlotResponse {

	private Long id;
	
	private LocalDateTime startTime;
	
	private LocalDateTime endTime;
	
}