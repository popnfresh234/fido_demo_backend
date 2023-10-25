package com.example.apilogin.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class LoginResponse extends Response{
    String accessToken;
    List<String> role;
    public LoginResponse(String message) {
        super(message);
    }
    public LoginResponse(String message, String accessToken, List<String> role) {
        super(message);
        this.accessToken = accessToken;
        this.role = role;
    }
}
