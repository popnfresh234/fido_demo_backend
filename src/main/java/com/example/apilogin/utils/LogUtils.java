package com.example.apilogin.utils;

import com.example.apilogin.entities.UserLogEntity;
import com.example.apilogin.service.UserLogRepository;
import com.example.apilogin.service.UserRepository;
import org.apache.catalina.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LogUtils {
    public static UserLogEntity buildLog(UserLogRepository repo,String ip, String msg, boolean success){
        UserLogEntity log = new UserLogEntity();
        log.setIp(ip);
        log.setLog(msg);
        log.setTimestamp(LocalDateTime.now());
        log.setSuccess(success);
        repo.save(log);
        return log;
    }
}
