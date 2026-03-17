package com.project.controller;

import com.project.entities.User;
import com.project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUser(){
        return userService.getAllUser();
    }

    @PostMapping
    public User createUser(@RequestBody User user){
        return userService.createUser(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
    }
    
    @PutMapping("/{id}")
    public void updateUser(@PathVariable Long id, @RequestBody User userDetail){
        userService.updateUser(id, userDetail);
    }


    @GetMapping("/search")
    public User findUserByUsername(@RequestParam String username){
        return userService.findUserByUsername(username);
    }

    @GetMapping("/{id}")
    public User findUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }



}
