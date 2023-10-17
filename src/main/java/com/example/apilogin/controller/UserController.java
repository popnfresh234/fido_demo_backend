package com.example.apilogin.controller;

import com.example.apilogin.entities.UserEntity;
import com.example.apilogin.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(path="/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<UserEntity> getAllUsers() {
        // This returns a JSON or XML with the users
        return userRepository.findAll();
    }

    @GetMapping(path = "/")
    public @ResponseBody UserEntity getUser(@RequestParam String email) {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            throw new DataAccessException("This user cannot be found") {
            };
        }
        return userRepository.findByEmail(email);
    }
}
