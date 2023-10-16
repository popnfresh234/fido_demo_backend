package com.example.apilogin.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse extends Response{
    String accessToken;
    public LoginResponse(String message) {
        super(message);
    }
    public LoginResponse(String message, String accessToken) {
        super(message);
        this.accessToken = accessToken;
    }
}
