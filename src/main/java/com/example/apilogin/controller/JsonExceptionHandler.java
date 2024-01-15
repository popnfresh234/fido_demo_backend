package com.example.apilogin.controller;

import com.example.apilogin.entities.UserLogEntity;
import com.example.apilogin.exceptions.*;
import com.example.apilogin.model.response.ErrorResponse;
import com.example.apilogin.services.UserLogService;
import com.example.apilogin.utils.ExceptionUtils;
import com.example.apilogin.utils.LogUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
// @ControllerAdvice
public class JsonExceptionHandler {
    private final UserLogService userLogService;

    public JsonExceptionHandler(UserLogService userLogService) {
        this.userLogService = userLogService;
    }

    //    ****************************
    //    Handle All Validation Exceptions
    //    ****************************
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<Object> handleValidationErrors(
            MethodArgumentNotValidException exception,
            HttpServletRequest req) {
        log.error(LogUtils.buildRouteLog(req.getRequestURI()));
        log.error("Validation Error");
        List<FieldError> fieldErrors = exception.getBindingResult()
                .getFieldErrors();
        String msg = fieldErrors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return buildResponseEntity(msg);
    }

    //    ****************************
    //    Handle Auth Exceptions
    //    ****************************
    @ExceptionHandler(AuthException.class)
    ResponseEntity<Object> handleFidoException(AuthException exception) {
        logErrors(
                exception,
                exception.getOperation());
        return buildResponseEntity(exception.getMessage());
    }

    //    ****************************
    //    Handle Recovery Exceptions
    //    ****************************
    @ExceptionHandler(RecoveryException.class)
    ResponseEntity<Object> handleRecoveryException(RecoveryException exception) {
        logErrors(
                exception,
                exception.getOperation());
        return buildResponseEntity(exception.getMessage());
    }

    //    ****************************
    //    Handle Fido2 Exceptions
    //    ****************************
    @ExceptionHandler(Fido2AuthException.class)
    ResponseEntity<Object> handleFidoException(Fido2AuthException exception) {
        logErrors(exception, exception.getOperation());
        return buildResponseEntity(exception.getMessage());
    }

    //    ****************************
    //    Handle UAF Exceptions
    //    ****************************
    @ExceptionHandler(UafException.class)
    ResponseEntity<Object> handleUafException(Fido2AuthException exception) {
        logErrors(
                exception,
                exception.getOperation());
        return buildResponseEntity(exception.getMessage());
    }


    //    ****************************
    //    Handle All Other Exceptions
    //    ****************************

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<Object> handleAllOtherErrors(
            HttpServletRequest req,
            Exception exception) {
        exception.printStackTrace();
        log.error(ExceptionUtils.getCause(exception));
        log.error("Path: " + LogUtils.buildRouteLog(req.getRequestURI()));
        log.error("Class: " + exception.getClass());
        String target = (String) req.getAttribute("account");
        UserLogEntity userLog = LogUtils.buildLog(
                userLogService,
                "general",
                target,
                req.getRemoteAddr(),
                exception.getMessage(),
                false);
        userLogService.save(userLog);
        log.error(exception.getMessage());

        return buildResponseEntity(exception.getMessage());
    }

    private void logErrors(GeneralException exception, String errorType) {
        log.error(errorType);
        log.error("Exception: " + exception.getMessage());
        UserLogEntity userLog = LogUtils.buildLog(
                userLogService,
                exception.getOperation(),
                exception.getTarget(),
                exception.getIp(),
                exception.getMessage(),
                false);
        userLogService.save(userLog);
    }

    private ResponseEntity<Object> buildResponseEntity(String msg) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponse(msg));
    }
}
