package com.example.apilogin.utils;

import com.example.apilogin.entities.UserLogEntity;
import com.example.apilogin.repositories.UserLogRepository;
import com.example.apilogin.services.UserLogService;

import java.time.LocalDateTime;

public class LogUtils {
    public static UserLogEntity buildLog(UserLogService userLogService, String operation, String target, String ip, String msg, boolean success){
        UserLogEntity log = new UserLogEntity();
        log.setOperation(operation);
        log.setIp(ip);
        log.setTarget(target);
        log.setLog(msg);
        log.setTimestamp(LocalDateTime.now());
        log.setSuccess(success);
        userLogService.save(log);
        return log;
    }
}
