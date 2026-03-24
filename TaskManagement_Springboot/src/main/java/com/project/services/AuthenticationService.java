package com.project.services;

import com.project.dto.request.AuthRequest;
import com.project.dto.response.AuthResponse;
import com.project.entities.User;
import com.project.repositories.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.security.crypto.password.PasswordEncoder;
import java.security.Key;
import java.util.Date;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    private final String SECRET_KEY = "secretkey124891574501984750298fhadufe";

    public AuthResponse register(User user) {
        User savedUser = userService.createUser(user);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setAuthenticate(true);
        authResponse.setToken(generateToken(savedUser.getUsername()));
        authResponse.setUserId(savedUser.getId());
        authResponse.setUsername(savedUser.getUsername());
        authResponse.setFullName(savedUser.getFullName());
        authResponse.setEmail(savedUser.getEmail());
        authResponse.setAvatar(savedUser.getAvatar());
        authResponse.setRole(savedUser.getRole());
        return authResponse;
    }

    public AuthResponse authenticate(AuthRequest request){

        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Sai username hoac password"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Sai username hoac password");
        }

        AuthResponse authResponse = new AuthResponse();
        authResponse.setAuthenticate(true);
        authResponse.setToken(generateToken(user.getUsername()));
        authResponse.setUserId(user.getId());
        authResponse.setUsername(user.getUsername());
        authResponse.setFullName(user.getFullName());
        authResponse.setEmail(user.getEmail());
        authResponse.setAvatar(user.getAvatar());
        authResponse.setRole(user.getRole());
        return authResponse;
    }

    private String generateToken(String username){

        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .signWith(key)
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .compact();
    }

}
