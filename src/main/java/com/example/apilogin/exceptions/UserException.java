package com.example.apilogin.exceptions;

import lombok.Builder;

public class UserException extends GeneralException{
    @Builder
    public UserException(String msg, String operation, String target, String ip) {
        super(msg, operation, target, ip);
    }
}
