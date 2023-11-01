package com.example.apilogin.exceptions;

public class AuthException extends GeneralException{
    public AuthException(String msg, String operation, String target, String ip) {
        super(msg, operation, target, ip);
    }
}
