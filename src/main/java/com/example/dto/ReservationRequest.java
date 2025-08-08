package com.example.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequest {

    @NotNull(message = "User ID required")
    private Long userId;

    @NotNull(message = "Room ID required")
    private Long roomId;

    @NotNull(message = "Slots required")
    private List<ReservationSlotRequest> slots;
}
