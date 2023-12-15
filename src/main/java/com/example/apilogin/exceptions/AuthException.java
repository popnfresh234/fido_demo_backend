package com.example.apilogin.exceptions;

import lombok.Builder;

public class AuthException extends GeneralException{
    @Builder
    public AuthException(String msg, String operation, String target, String ip) {
        super(msg, operation, target, ip);
    }
}
