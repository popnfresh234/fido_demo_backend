package com.example.apilogin.exceptions;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class GeneralException extends RuntimeException {
    private final String operation;
    private final String target;
    private final String ip;

    public GeneralException(String msg, String operation, String target, String ip){
        super(msg);
        this.operation = operation;
        this.target = target;
        this.ip = ip;
    }
}
