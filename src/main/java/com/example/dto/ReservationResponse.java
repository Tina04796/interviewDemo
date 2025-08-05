package com.example.dto;

import java.time.LocalDateTime;

import com.example.model.ReservationStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponse {

	private Long id;

	private Long userId;

	private String username;

	private Long roomId;

	private String roomName;

	private LocalDateTime startTime;

	private LocalDateTime endTime;
	
	private ReservationStatus status;

}
