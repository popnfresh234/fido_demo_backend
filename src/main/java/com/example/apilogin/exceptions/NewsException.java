package com.example.apilogin.exceptions;

public class NewsException extends GeneralException{
    public NewsException(String msg, String operation, String target, String ip) {
        super(msg, operation, target, ip);
    }
}
