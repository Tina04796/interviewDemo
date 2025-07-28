package com.example.service;

import java.util.List;
import java.util.Optional;

import com.example.dto.ReservationRequest;
import com.example.dto.ReservationResponse;

public interface ReservationService {

    // 建立預約
    ReservationResponse createReservation(ReservationRequest request);

    // 查詢所有預約
    List<ReservationResponse> getAllReservations();

    // 用 ID 查詢單筆預約
    Optional<ReservationResponse> getReservationById(Long id);

    // 更新預約
    Optional<ReservationResponse> updateReservation(Long id, ReservationRequest request);

    // 刪除預約
    boolean deleteReservation(Long id);

    // 取消預約（標記為已取消，但不刪除）
    Optional<ReservationResponse> cancelReservation(Long id);
}
