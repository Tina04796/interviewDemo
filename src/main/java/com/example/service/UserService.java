package com.example.service;

import com.example.dto.*;
import com.example.model.Role;

import java.util.Optional;

public interface UserService {

    boolean existsByUsername(String username);

    UserResponse register(RegisterRequest request);

    String login(LoginRequest request);

    Optional<UserResponse> getUserById(Long id);

    Optional<UserResponse> updateProfile(Long id, UpdateProfileRequest request);

    boolean updatePassword(Long id, UpdatePasswordRequest request);

    boolean deleteUser(Long id);
    
    Optional<UserResponse> upgradeUserRole(Long userId, Role newRole);

}
