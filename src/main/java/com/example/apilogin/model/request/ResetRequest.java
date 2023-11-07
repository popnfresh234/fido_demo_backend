package com.example.apilogin.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ResetRequest {
    private final String account;
    private final String code;
    private final String password;
}
