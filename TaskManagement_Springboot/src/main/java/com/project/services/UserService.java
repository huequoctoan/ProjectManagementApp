package com.project.services;

import com.project.entities.User;
import com.project.repositories.NotificationRepository;
import com.project.repositories.ProjectMemberRepository;
import com.project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    public List<User> getAllUser(){
        return userRepository.findAll();
    }

    public User createUser(User user){

        if(userRepository.findByUsername(user.getUsername()).isPresent()){
            throw new RuntimeException("Username already exists");
        }

        return userRepository.save(user);
    }

    public User getUserById(Long id){
        return userRepository.findById(id).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Khong tim thay User"));
    }

    public User findUserByUsername(@RequestParam String username){
        return userRepository.findByUsername(username).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Khong tim thay User"));
    }

    public void updateUser(Long id, User userDetail) {

        User existingUser = userRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Khong tim thay User"));

        existingUser.setEmail(userDetail.getEmail());
        
        // Cập nhật thông tin mới
        if(userDetail.getFullName() != null) existingUser.setFullName(userDetail.getFullName());
        if(userDetail.getDateOfBirth() != null) existingUser.setDateOfBirth(userDetail.getDateOfBirth());
        if(userDetail.getPhoneNumber() != null) existingUser.setPhoneNumber(userDetail.getPhoneNumber());
        if(userDetail.getAvatar() != null) existingUser.setAvatar(userDetail.getAvatar());
        
        // Mật khẩu (Nếu client cho phép đổi)
        if(userDetail.getPassword() != null && !userDetail.getPassword().isBlank()){
             existingUser.setPassword(userDetail.getPassword());
        }

        userRepository.save(existingUser);
    }

    public void deleteUser(Long id){
        if(!userRepository.existsById(id)) return;
        
        notificationRepository.deleteAll(notificationRepository.findByUserId(id));

        userRepository.deleteById(id);
    }

}
