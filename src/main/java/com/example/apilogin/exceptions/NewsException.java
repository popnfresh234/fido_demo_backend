package com.example.apilogin.exceptions;

import lombok.Builder;

public class NewsException extends GeneralException{
    @Builder
    public NewsException(String msg, String operation, String target, String ip) {
        super(msg, operation, target, ip);
    }
}
