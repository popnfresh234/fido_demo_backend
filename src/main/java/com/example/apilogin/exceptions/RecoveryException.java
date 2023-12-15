package com.example.apilogin.exceptions;

import lombok.Builder;

public class RecoveryException extends GeneralException{
    @Builder
    public RecoveryException(
            String msg,
            String operation,
            String target,
            String ip) {
        super(
                msg,
                operation,
                target,
                ip);
    }
}
