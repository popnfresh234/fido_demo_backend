package com.example.apilogin.controller;

import com.example.apilogin.entities.UserEntity;
import com.example.apilogin.security.UserPrincipal;
import com.example.apilogin.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@CrossOrigin
public class HelloController
{
    @Autowired
    private UserRepository userRepository;
    @GetMapping("/")
    public String greeting(){
        return "Hello World!";
    }

    @GetMapping("secured")
    public String secured(@AuthenticationPrincipal UserPrincipal principal){
        return "If you see this, you are authenticated as user " + principal.getEmail();
    }

    @CrossOrigin
    @GetMapping(path = "/all")
    public @ResponseBody Iterable<UserEntity> getAllUsers() {
        // This returns a JSON or XML with the users
        return userRepository.findAll();
    }

    @GetMapping(path = "/user/")
    public @ResponseBody UserEntity getUser(@RequestParam String email) {
        return userRepository.findByEmail(email);
    }
}
