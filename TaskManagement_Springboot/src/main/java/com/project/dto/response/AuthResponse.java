package com.project.dto.response;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private boolean authenticate;
}
