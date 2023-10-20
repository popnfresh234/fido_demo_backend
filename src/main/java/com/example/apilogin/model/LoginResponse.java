package com.example.apilogin.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse extends Response{
    String accessToken;
    String role;
    public LoginResponse(String message) {
        super(message);
    }
    public LoginResponse(String message, String accessToken, String role) {
        super(message);
        this.accessToken = accessToken;
        this.role = role;
    }
}
