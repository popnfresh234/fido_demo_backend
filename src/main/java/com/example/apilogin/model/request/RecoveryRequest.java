package com.example.apilogin.model.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecoveryRequest {
    private final String account;
    private final String code;
}
