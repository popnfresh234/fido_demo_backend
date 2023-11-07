package com.example.apilogin.services;

import com.example.apilogin.entities.UserLogEntity;
import com.example.apilogin.repositories.UserLogRepository;
import org.springframework.stereotype.Service;

@Service
public class UserLogService {
    private final UserLogRepository userLogRepository;

    public UserLogService(UserLogRepository userLogRepository){
        this.userLogRepository = userLogRepository;
    }

    public void save(UserLogEntity userLogEntity){
        userLogRepository.save(userLogEntity);
    }
}
