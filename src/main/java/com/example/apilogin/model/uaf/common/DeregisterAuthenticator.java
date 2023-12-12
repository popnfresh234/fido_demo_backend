package com.example.apilogin.model.uaf.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeregisterAuthenticator {
    private String aaid;
    private String keyID;
}
