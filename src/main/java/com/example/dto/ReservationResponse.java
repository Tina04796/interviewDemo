package com.example.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.example.model.ReservationStatus;
import com.example.model.ReservationPaymentStatus;
import com.example.model.PaymentMethod;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponse {

    private Long id;

    private Long userId;

    private String username;

    private Long roomId;

    private String roomName;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private ReservationStatus status;

    private ReservationPaymentStatus paymentStatus;

    private PaymentMethod paymentMethod;

    private BigDecimal paymentAmount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime paymentDueAt;

    private LocalDateTime paidAt;

    private LocalDateTime cancelledAt;

    private LocalDateTime refundedAt;

    private String note;

    private String statusHistoryJson;

    private List<ReservationSlotResponse> slots;
}
