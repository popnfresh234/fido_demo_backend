package com.example.apilogin.controller;

import com.example.apilogin.entities.UserLogEntity;
import com.example.apilogin.exceptions.AuthException;
import com.example.apilogin.model.ErrorResponse;
import com.example.apilogin.service.UserLogRepository;
import com.example.apilogin.utils.LogUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Log4j2
@ControllerAdvice


public class JsonExceptionHandler {

    @Autowired
    UserLogRepository userLogRepository;


    @ExceptionHandler(AuthException.class)
    ResponseEntity<Object> handleLoginException(AuthException exception) {
        UserLogEntity userLog = LogUtils.buildLog(
                userLogRepository,
                exception.getOperation(),
                exception.getTarget(),
                exception.getIp(),
                exception.getMessage(),
                false);
        userLogRepository.save(userLog);
        return buildResponseEntity(exception);
    }

    //    Catch all
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<Object> handleAllOtherErrors(HttpServletRequest req, Exception exception) {
        String target = (String) req.getAttribute("account");
        UserLogEntity userLog = LogUtils.buildLog(userLogRepository, "general", target, req.getRemoteAddr(), exception.getMessage(), false);
        userLogRepository.save(userLog);
        log.error(exception.getMessage());

        return buildResponseEntity(exception);
    }

    private ResponseEntity<Object> buildResponseEntity(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponse(e.getMessage()));
    }
}
