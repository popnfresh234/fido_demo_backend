package com.example.apilogin.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginRequest {
    //TODO:  Add validation front and back
    private String email;
    private String password;
}
