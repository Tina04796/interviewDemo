package com.example.dto;

import com.example.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;

    private String username;

    private Role role;

    private String email;

}
