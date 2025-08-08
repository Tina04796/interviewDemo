package com.example.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.dto.ReservationRequest;
import com.example.dto.ReservationResponse;
import com.example.dto.ReservationSlotRequest;
import com.example.dto.ReservationSlotResponse;
import com.example.model.Reservation;
import com.example.model.ReservationPaymentStatus;
import com.example.model.ReservationSlot;
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

		if (request.getSlots() == null || request.getSlots().isEmpty()) {
			throw new RuntimeException("At least one slot is required");
		}

		LocalDateTime startTime = request.getSlots().stream().map(ReservationSlotRequest::getStartTime)
				.min(LocalDateTime::compareTo).orElseThrow(() -> new RuntimeException("Slots must have start time"));
		LocalDateTime endTime = request.getSlots().stream().map(ReservationSlotRequest::getEndTime)
				.max(LocalDateTime::compareTo).orElseThrow(() -> new RuntimeException("Slots must have end time"));

		Reservation reservation = Reservation.builder().user(user).room(room).startTime(startTime).endTime(endTime)
				.status(ReservationStatus.CREATED) // 預設
				.paymentStatus(ReservationPaymentStatus.UNPAID) // 預設未付款
				.paymentMethod(null) // 還沒選付款方式
				.paymentAmount(BigDecimal.ZERO).build();

		List<ReservationSlot> slots = request.getSlots().stream().map(slotReq -> {
			ReservationSlot slot = new ReservationSlot();
			slot.setReservation(reservation);
			slot.setRoom(room);
			slot.setStartTime(slotReq.getStartTime());
			slot.setEndTime(slotReq.getEndTime());
			return slot;
		}).collect(Collectors.toList());

		reservation.setSlots(slots);

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

			if (request.getSlots() == null || request.getSlots().isEmpty()) {
				throw new RuntimeException("At least one slot is required");
			}

			LocalDateTime startTime = request.getSlots().stream().map(ReservationSlotRequest::getStartTime)
					.min(LocalDateTime::compareTo)
					.orElseThrow(() -> new RuntimeException("Slots must have start time"));
			LocalDateTime endTime = request.getSlots().stream().map(ReservationSlotRequest::getEndTime)
					.max(LocalDateTime::compareTo).orElseThrow(() -> new RuntimeException("Slots must have end time"));

			reservation.setUser(user);
			reservation.setRoom(room);
			reservation.setStartTime(startTime);
			reservation.setEndTime(endTime);
//			清除舊 slots
			reservation.getSlots().clear();

//			新增 slots
			List<ReservationSlot> newSlots = request.getSlots().stream().map(slotReq -> {
				ReservationSlot slot = new ReservationSlot();
				slot.setReservation(reservation);
				slot.setRoom(room);
				slot.setStartTime(slotReq.getStartTime());
				slot.setEndTime(slotReq.getEndTime());
				return slot;
			}).collect(Collectors.toList());

			reservation.getSlots().addAll(newSlots);

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

	@Override
	public Optional<ReservationResponse> confirmReservation(Long id) {
		return reservationRepository.findById(id).map(reservation -> {
//			CREATED 狀態才允許付款確認
			if (reservation.getStatus() != ReservationStatus.CREATED) {
				throw new IllegalStateException("Reservation cannot be confirmed");
			}
			reservation.setStatus(ReservationStatus.CONFIRMED);
			reservation.setPaymentStatus(ReservationPaymentStatus.PAID);
			reservation.setPaidAt(LocalDateTime.now());
			reservation.setUpdatedAt(LocalDateTime.now());
			Reservation updated = reservationRepository.save(reservation);
			return convertToResponse(updated);
		});
	}

	@Override
	public Optional<ReservationResponse> refundReservation(Long id) {
		return reservationRepository.findById(id).map(reservation -> {
			reservation.setStatus(ReservationStatus.REFUNDED);
			reservation.setRefundedAt(LocalDateTime.now());
			Reservation updated = reservationRepository.save(reservation);
			return convertToResponse(updated);
		});
	}

	private ReservationResponse convertToResponse(Reservation reservation) {
		List<ReservationSlotResponse> slotResponses = reservation
				.getSlots().stream().map(slot -> ReservationSlotResponse.builder().id(slot.getId())
						.startTime(slot.getStartTime()).endTime(slot.getEndTime()).build())
				.collect(Collectors.toList());

		return ReservationResponse.builder().id(reservation.getId()).userId(reservation.getUser().getId())
				.username(reservation.getUser().getUsername()).roomId(reservation.getRoom().getId())
				.roomName(reservation.getRoom().getName()).startTime(reservation.getStartTime())
				.endTime(reservation.getEndTime()).status(reservation.getStatus())
				.paymentStatus(reservation.getPaymentStatus()).paymentMethod(reservation.getPaymentMethod())
				.paymentAmount(reservation.getPaymentAmount()).createdAt(reservation.getCreatedAt())
				.updatedAt(reservation.getUpdatedAt()).paymentDueAt(reservation.getPaymentDueAt())
				.paidAt(reservation.getPaidAt()).cancelledAt(reservation.getCancelledAt())
				.refundedAt(reservation.getRefundedAt()).note(reservation.getNote())
				.statusHistoryJson(reservation.getStatusHistoryJson()).slots(slotResponses).build();
	}

}
