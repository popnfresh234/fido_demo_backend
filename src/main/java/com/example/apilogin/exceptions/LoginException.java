package com.example.apilogin.exceptions;

import lombok.Builder;

public class LoginException extends GeneralException{
    @Builder
    public LoginException(String msg, String operation, String target, String ip) {
        super(msg, operation, target, ip);
    }
}
