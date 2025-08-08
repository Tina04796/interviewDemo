package com.example.dto;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationSlotRequest {

    @NotNull(message = "Start time required")
    private LocalDateTime startTime;

    @NotNull(message = "End time required")
    private LocalDateTime endTime;
    
}