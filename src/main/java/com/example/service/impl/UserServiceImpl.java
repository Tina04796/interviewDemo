package com.example.service.impl;

import com.example.dto.LoginRequest;
import com.example.dto.RegisterRequest;
import com.example.dto.UpdatePasswordRequest;
import com.example.dto.UpdateProfileRequest;
import com.example.dto.UserResponse;
import com.example.model.Role;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.security.JwtUtil;
import com.example.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	@Override
	public boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	@Override
	public UserResponse register(RegisterRequest request) {
		String encodedPassword = passwordEncoder.encode(request.getPassword());

		User user = User.builder().username(request.getUsername()).password(encodedPassword).role(Role.USER)
				.email(request.getEmail()).build();

		User saved = userRepository.save(user);
		return convertToResponse(saved);
	}

	@Override
	public String login(LoginRequest request) {
		Optional<User> userOpt = userRepository.findByUsernameOrEmail(request.getUsernameOrEmail());
		if (userOpt.isPresent()) {
			User user = userOpt.get();
			if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
				return jwtUtil.generateToken(user.getUsername(), user.getRole());
			}
		}
		return null;
	}

	@Override
	public Optional<UserResponse> getUserById(Long id) {
		return userRepository.findById(id).map(this::convertToResponse);
	}

	@Override
	public Optional<UserResponse> updateProfile(Long id, UpdateProfileRequest request) {
		return userRepository.findById(id).map(user -> {
			user.setUsername(request.getUsername());
			user.setEmail(request.getEmail());
			User updated = userRepository.save(user);
			return convertToResponse(updated);
		});
	}

	@Override
	public boolean updatePassword(Long id, UpdatePasswordRequest request) {
		return userRepository.findById(id).map(user -> {
			if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
				return false;
			}
			user.setPassword(passwordEncoder.encode(request.getNewPassword()));
			userRepository.save(user);
			return true;
		}).orElse(false);
	}

	@Override
	public boolean deleteUser(Long id) {
		if (userRepository.existsById(id)) {
			userRepository.deleteById(id);
			return true;
		}
		return false;
	}
	
	@Override
	public Optional<UserResponse> upgradeUserRole(Long userId, Role newRole) {
	    return userRepository.findById(userId).map(user -> {
	        user.setRole(newRole);
	        User updated = userRepository.save(user);
	        return convertToResponse(updated);
	    });
	}

	private UserResponse convertToResponse(User user) {
		return new UserResponse(user.getId(), user.getUsername(), user.getRole(), user.getEmail());
	}
}
