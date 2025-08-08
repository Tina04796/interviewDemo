package com.example.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.example.dto.ReservationRequest;
import com.example.dto.ReservationResponse;
import com.example.service.ReservationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
public class ReservationController {

	private final ReservationService reservationService;

	@GetMapping
	public ResponseEntity<List<ReservationResponse>> getAllReservations() {
		List<ReservationResponse> reservations = reservationService.getAllReservations();
		return ResponseEntity.ok(reservations);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ReservationResponse> getReservationById(@PathVariable Long id) {
		return reservationService.getReservationById(id).map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<?> createReservation(@Valid @RequestBody ReservationRequest reservationRequest,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			String errorMsg = bindingResult.getFieldError().getDefaultMessage();
			return ResponseEntity.badRequest().body(Map.of("error", errorMsg));
		}
		ReservationResponse saved = reservationService.createReservation(reservationRequest);
		return ResponseEntity.created(URI.create("/api/reservations/" + saved.getId())).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateReservation(@PathVariable Long id,
			@Valid @RequestBody ReservationRequest reservationRequest, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			String errorMsg = bindingResult.getFieldError().getDefaultMessage();
			return ResponseEntity.badRequest().body(Map.of("error", errorMsg));
		}
		return reservationService.updateReservation(id, reservationRequest).map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteReservation(@PathVariable Long id) {
		boolean deleted = reservationService.deleteReservation(id);
		if (deleted) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.status(404).body(Map.of("error", "Booking not found"));
		}
	}

	@PatchMapping("/{id}/cancel")
	public ResponseEntity<ReservationResponse> cancelReservation(@PathVariable Long id) {
		return reservationService.cancelReservation(id).map(ResponseEntity::ok)
				.orElse(ResponseEntity.status(404).build());
	}

	@PatchMapping("/{id}/confirm")
	public ResponseEntity<?> confirmReservation(@PathVariable Long id) {
		try {
			return reservationService.confirmReservation(id).map(ResponseEntity::ok)
					.orElse(ResponseEntity.notFound().build());
		} catch (IllegalStateException e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
		}
	}

}
