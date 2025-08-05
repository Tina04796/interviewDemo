package com.example.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.dto.ReservationRequest;
import com.example.dto.ReservationResponse;
import com.example.model.Reservation;
import com.example.model.ReservationStatus;
import com.example.model.Room;
import com.example.model.User;
import com.example.repository.ReservationRepository;
import com.example.repository.RoomRepository;
import com.example.repository.UserRepository;
import com.example.service.ReservationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

	private final ReservationRepository reservationRepository;
	private final UserRepository userRepository;
	private final RoomRepository roomRepository;

	@Override
	public ReservationResponse createReservation(ReservationRequest request) {
		User user = userRepository.findById(request.getUserId())
				.orElseThrow(() -> new RuntimeException("User not found"));
		Room room = roomRepository.findById(request.getRoomId())
				.orElseThrow(() -> new RuntimeException("Room not found"));

		Reservation reservation = Reservation.builder().user(user).room(room).startTime(request.getStartTime())
				.endTime(request.getEndTime()).build();

		Reservation saved = reservationRepository.save(reservation);
		return convertToResponse(saved);
	}

	@Override
	public List<ReservationResponse> getAllReservations() {
		return reservationRepository.findAll().stream().map(this::convertToResponse).collect(Collectors.toList());
	}

	@Override
	public Optional<ReservationResponse> getReservationById(Long id) {
		return reservationRepository.findById(id).map(this::convertToResponse);
	}

	@Override
	public Optional<ReservationResponse> updateReservation(Long id, ReservationRequest request) {
		return reservationRepository.findById(id).map(reservation -> {
			User user = userRepository.findById(request.getUserId())
					.orElseThrow(() -> new RuntimeException("User not found"));
			Room room = roomRepository.findById(request.getRoomId())
					.orElseThrow(() -> new RuntimeException("Room not found"));

			reservation.setUser(user);
			reservation.setRoom(room);
			reservation.setStartTime(request.getStartTime());
			reservation.setEndTime(request.getEndTime());
			Reservation updated = reservationRepository.save(reservation);
			return convertToResponse(updated);
		});
	}

	@Override
	public boolean deleteReservation(Long id) {
		return reservationRepository.findById(id).map(reservation -> {
			reservationRepository.delete(reservation);
			return true;
		}).orElse(false);
	}

	@Override
	public Optional<ReservationResponse> cancelReservation(Long id) {
		return reservationRepository.findById(id).map(reservation -> {
			reservation.setStatus(ReservationStatus.CANCELLED);
			reservation.setCancelledAt(LocalDateTime.now());
			Reservation updated = reservationRepository.save(reservation);
			return convertToResponse(updated);
		});
	}

	private ReservationResponse convertToResponse(Reservation reservation) {
		ReservationResponse response = new ReservationResponse();
		response.setId(reservation.getId());
		response.setUserId(reservation.getUser().getId());
		response.setUsername(reservation.getUser().getUsername());
		response.setRoomId(reservation.getRoom().getId());
		response.setRoomName(reservation.getRoom().getName());
		response.setStartTime(reservation.getStartTime());
		response.setEndTime(reservation.getEndTime());
		response.setStatus(reservation.getStatus());
		return response;
	}
}
