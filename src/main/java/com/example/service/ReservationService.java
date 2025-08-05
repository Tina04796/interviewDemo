package com.example.service;

import java.util.List;
import java.util.Optional;

import com.example.dto.ReservationRequest;
import com.example.dto.ReservationResponse;

public interface ReservationService {

    ReservationResponse createReservation(ReservationRequest request);

    List<ReservationResponse> getAllReservations();

    Optional<ReservationResponse> getReservationById(Long id);

    Optional<ReservationResponse> updateReservation(Long id, ReservationRequest request);

    boolean deleteReservation(Long id);

    // 標記為取消但不刪除
    Optional<ReservationResponse> cancelReservation(Long id);
}
