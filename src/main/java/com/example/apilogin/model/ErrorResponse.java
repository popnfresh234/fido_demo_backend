package com.example.apilogin.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public class ErrorResponse extends Response{

    public ErrorResponse(String message) {
        super(message);
    }
}
