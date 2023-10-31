package com.example.apilogin.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecoveryRequest {
    private final String account;
    private final String code;
}
