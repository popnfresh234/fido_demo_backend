package com.example.apilogin.exceptions;

public class UserEditException extends GeneralException{
    public UserEditException(String msg, String operation, String target, String ip) {
        super(msg, operation, target, ip);
    }
}
