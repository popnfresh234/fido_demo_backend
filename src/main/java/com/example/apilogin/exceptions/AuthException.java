package com.example.apilogin.exceptions;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
public class AuthException extends RuntimeException {
    private String operation;
    private String target;
    private String ip;

    public AuthException(String msg){
        super(msg);
    }
}
