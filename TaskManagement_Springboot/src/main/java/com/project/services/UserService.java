package com.project.services;

import com.project.dao.UserDAO;
import com.project.entities.User;
import com.project.repositories.NotificationRepository;
import com.project.repositories.ProjectMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    public List<User> getAllUser(){
        return userDAO.findAll();
    }

    public User createUser(User user){

        if(userDAO.findByUsername(user.getUsername()).isPresent()){
            throw new RuntimeException("Username already exists");
        }
        
        // Encode password before saving
        if(user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        return userDAO.save(user);
    }

    public User getUserById(Long id){
        return userDAO.findById(id).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Khong tim thay User"));
    }

    public User findUserByUsername(@RequestParam String username){
        return userDAO.findByUsername(username).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Khong tim thay User"));
    }

    public void updateUser(Long id, User userDetail) {

        User existingUser = userDAO.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Khong tim thay User"));

        existingUser.setEmail(userDetail.getEmail());
        
        // Cập nhật thông tin mới
        if(userDetail.getFullName() != null) existingUser.setFullName(userDetail.getFullName());
        if(userDetail.getDateOfBirth() != null) existingUser.setDateOfBirth(userDetail.getDateOfBirth());
        if(userDetail.getPhoneNumber() != null) existingUser.setPhoneNumber(userDetail.getPhoneNumber());
        if(userDetail.getAvatar() != null) existingUser.setAvatar(userDetail.getAvatar());
        if(userDetail.getRole() != null) existingUser.setRole(userDetail.getRole());
        
        // Mật khẩu (Nếu client cho phép đổi)
        if(userDetail.getPassword() != null && !userDetail.getPassword().isBlank()){
             existingUser.setPassword(passwordEncoder.encode(userDetail.getPassword()));
        }

        userDAO.save(existingUser);
    }

    public void deleteUser(Long id){
        if(userDAO.findById(id).isEmpty()) return;
        
        notificationRepository.deleteAll(notificationRepository.findByUserId(id));

        userDAO.deleteById(id);
    }

}
