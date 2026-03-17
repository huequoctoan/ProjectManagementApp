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

import java.security.Key;
import java.util.Date;

@Service
public class AuthenticationService {
    @Autowired
    UserRepository userRepository;

    private final String SECRET_KEY = "secretkey124891574501984750298fhadufe";

    public AuthResponse authenticate(AuthRequest request){

        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Sai username hoac password"));

        if(!user.getPassword().equals(request.getPassword())){
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
