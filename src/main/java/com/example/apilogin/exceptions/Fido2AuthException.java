package com.example.apilogin.exceptions;

import lombok.Builder;

public class Fido2AuthException extends GeneralException{
    @Builder
    public Fido2AuthException(String msg, String operation, String target, String ip) {
        super(msg, operation, target, ip);
    }
}
