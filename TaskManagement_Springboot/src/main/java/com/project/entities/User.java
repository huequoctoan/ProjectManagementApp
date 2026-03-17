package com.project.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    
    private String email;
    private String password;
    
    // Profile Fields
    private String fullName;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    
    @Column(columnDefinition = "LONGTEXT")
    private String avatar;
}