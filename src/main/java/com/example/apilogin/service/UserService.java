package com.example.apilogin.service;

import com.example.apilogin.entities.UserEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private static final String EXISTING_EMAIL = "test@test.com";

    public Optional<UserEntity> findByEmail(String email){
        // TODO: Move this to a database
        if(!EXISTING_EMAIL.equalsIgnoreCase(email)) return Optional.empty();
        var user = new UserEntity();
        user.setId(1L);
        user.setEmail(EXISTING_EMAIL);
        user.setPassword("$2a$12$VE6Uk6vO6HS3GwwXSHeEzOQttwMoiHSpltkOiyMqePZnHnZRpkxHi");
        user.setRole("ROLE_ADMIN");
        user.setExtraInfo(("My nice admin"));
        return Optional.of(user);
    }
}
