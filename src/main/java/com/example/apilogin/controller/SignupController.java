package com.example.apilogin.controller;

import com.example.apilogin.entities.UserEntity;
import com.example.apilogin.model.Response;
import com.example.apilogin.model.SignupResponse;
import com.example.apilogin.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@CrossOrigin
public class SignupController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @PostMapping("/signup")
    public Response signup(@RequestParam String name,
                           @RequestParam String email,
                           @RequestParam String password) {
        var user = new UserEntity();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("ROLE_ADMIN");
        user.setExtraInfo(("My nice admin"));
        if(userRepository.findByEmail(email)!= null){
            throw new DataAccessException("This user already exists") {
            };
        }else {
            userRepository.save(user);
            return new SignupResponse("Saved");
        }
    }
}

