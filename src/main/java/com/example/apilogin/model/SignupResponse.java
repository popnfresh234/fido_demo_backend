package com.example.apilogin.model;

import lombok.Builder;
import lombok.Getter;

public class SignupResponse extends Response {
    public SignupResponse(String message) {
        super(message);
    }
}
