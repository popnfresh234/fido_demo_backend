package com.example.apilogin.utils;

import com.example.apilogin.entities.UserLogEntity;
import com.example.apilogin.security.JwtToPrincipalConverter;
import com.example.apilogin.security.UserPrincipal;
import com.example.apilogin.services.UserLogService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.LocalDateTime;

public class LogUtils {


    public static final String OPERATION_LOGIN = "login";
    public static final String OPERATION_SIGNUP = "signup";
    public static final String OPERATION_RECOVERY_REQUEST = "recovery_request";
    public static final String OPERATION_RECOVERY_VERIFY = "recovery_verify";
    public static final String OPERATION_RECOVERY_RESET = "recovery_reset";

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
