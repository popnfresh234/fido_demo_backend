package com.example.apilogin.services;

import com.example.apilogin.entities.UserLogEntity;
import com.example.apilogin.repositories.UserLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserLogService {
    @Autowired
    private UserLogRepository userLogRepository;

    public void save(UserLogEntity userLogEntity){
        userLogRepository.save(userLogEntity);
    }
}
