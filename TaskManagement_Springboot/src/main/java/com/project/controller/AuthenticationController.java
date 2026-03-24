package com.project.controller;

import com.project.dto.request.AuthRequest;
import com.project.dto.response.AuthResponse;
import com.project.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/log-in")
    public AuthResponse authenticate(@RequestBody AuthRequest request){
        return authenticationService.authenticate(request);
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody com.project.entities.User user){
        return authenticationService.register(user);
    }
}
