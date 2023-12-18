package com.example.apilogin.exceptions;

import lombok.Builder;

public class UafException extends GeneralException{
    @Builder
    public UafException(String msg, String operation, String target, String ip) {
        super(msg, operation, target, ip);
    }
}
