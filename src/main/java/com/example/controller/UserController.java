package com.example.controller;

import com.example.dto.LoginRequest;
import com.example.dto.RegisterRequest;
import com.example.dto.UpdatePasswordRequest;
import com.example.dto.UpdateProfileRequest;
import com.example.dto.UserResponse;
import com.example.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:8080")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping("/register")
	public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request, BindingResult result) {
		if (result.hasErrors()) {
			return ResponseEntity.badRequest().body(Map.of("error", result.getFieldError().getDefaultMessage()));
		}

		if (userService.existsByUsername(request.getUsername())) {
			return ResponseEntity.badRequest().body(Map.of("error", "此帳號已有人使用"));
		}

		UserResponse userResponse = userService.register(request);
		return ResponseEntity.ok(userResponse);
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, BindingResult result) {
		if (result.hasErrors()) {
			return ResponseEntity.badRequest().body(Map.of("error", result.getFieldError().getDefaultMessage()));
		}

		String token = userService.login(request);
		if (token != null) {
			return ResponseEntity.ok(Map.of("token", token));
		}
		return ResponseEntity.status(401).body(Map.of("error", "帳號或密碼錯誤"));
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
		return userService.getUserById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/{id}/profile")
	public ResponseEntity<?> updateProfile(@PathVariable Long id, @Valid @RequestBody UpdateProfileRequest request,
			BindingResult result) {
		if (result.hasErrors()) {
			return ResponseEntity.badRequest().body(Map.of("error", result.getFieldError().getDefaultMessage()));
		}

		return userService.updateProfile(id, request).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/{id}/password")
	public ResponseEntity<?> updatePassword(@PathVariable Long id, @Valid @RequestBody UpdatePasswordRequest request,
			BindingResult result) {
		if (result.hasErrors()) {
			return ResponseEntity.badRequest().body(Map.of("error", result.getFieldError().getDefaultMessage()));
		}

		boolean updated = userService.updatePassword(id, request);
		if (updated) {
			return ResponseEntity.ok(Map.of("message", "密碼更新成功"));
		} else {
			return ResponseEntity.status(400).body(Map.of("error", "舊密碼錯誤"));
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Long id) {
		boolean deleted = userService.deleteUser(id);
		if (deleted) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}
}
