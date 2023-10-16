package com.example.apilogin.controller;

import com.example.apilogin.entities.UserEntity;
import com.example.apilogin.security.UserPrincipal;
import com.example.apilogin.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@CrossOrigin
public class HelloController {
    @Autowired
    private UserRepository userRepository;

    @CrossOrigin
    @GetMapping(path = "/all")
    public @ResponseBody Iterable<UserEntity> getAllUsers() {
        // This returns a JSON or XML with the users
        return userRepository.findAll();
    }

    @GetMapping(path = "/user/")
    public @ResponseBody UserEntity getUser(@RequestParam String email) {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            throw new DataAccessException("This user cannot be found") {
            };
        }
        return userRepository.findByEmail(email);
    }
}
