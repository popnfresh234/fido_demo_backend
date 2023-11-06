package com.example.apilogin.services;

import com.example.apilogin.entities.UserEntity;
import com.example.apilogin.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Optional<UserEntity> findByAccount(String account){
        return userRepository.findByAccount(account);
    }

    public Optional<UserEntity> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public Optional<UserEntity> findById(Integer id){
        return userRepository.findById(id);
    }

    public UserEntity save(UserEntity user){
        return userRepository.save(user);
    }
}
