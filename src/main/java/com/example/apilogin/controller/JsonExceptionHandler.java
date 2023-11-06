package com.example.apilogin.controller;

import com.example.apilogin.entities.UserLogEntity;
import com.example.apilogin.exceptions.AuthException;
import com.example.apilogin.exceptions.GeneralException;
import com.example.apilogin.model.ErrorResponse;
import com.example.apilogin.service.UserLogRepository;
import com.example.apilogin.utils.LogUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@ControllerAdvice


public class JsonExceptionHandler {

    @Autowired
    UserLogRepository userLogRepository;

    @ExceptionHandler(GeneralException.class)
    ResponseEntity<Object> handleLoginException(GeneralException exception) {
        log.error("Exception: " + exception.getMessage());
        UserLogEntity userLog = LogUtils.buildLog(
                userLogRepository,
                exception.getOperation(),
                exception.getTarget(),
                exception.getIp(),
                exception.getMessage(),
                false);
        userLogRepository.save(userLog);
        return buildResponseEntity(exception.getMessage());
    }

//    Handler for method validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<Object> handleValidationErrors(MethodArgumentNotValidException exception, HttpServletRequest httpServletRequest){
        log.error("Validation Error");
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        String msg = fieldErrors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return buildResponseEntity(msg);
    }

    //    Catch all
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<Object> handleAllOtherErrors(HttpServletRequest req, Exception exception) {
        log.error("Class: " + exception.getClass());
        String target = (String) req.getAttribute("account");
        UserLogEntity userLog = LogUtils.buildLog(userLogRepository, "general", target, req.getRemoteAddr(), exception.getMessage(), false);
        userLogRepository.save(userLog);
        log.error(exception.getMessage());

        return buildResponseEntity(exception.getMessage());
    }

    private ResponseEntity<Object> buildResponseEntity(String msg) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponse(msg));
    }
}
