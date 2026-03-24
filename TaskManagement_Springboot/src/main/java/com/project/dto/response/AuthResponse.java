package com.project.dto.response;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private boolean authenticate;
    private Long userId;
    private String username;
    private String fullName;
    private String email;
    private String avatar;
    private String role;
}
