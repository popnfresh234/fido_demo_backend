package com.example.apilogin.model.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginRequest {
    private String account;
    private String password;
}
