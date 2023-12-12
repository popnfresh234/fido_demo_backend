package com.example.apilogin.model.uaf.request.auth.do_auth_req;

import com.example.apilogin.model.uaf.common.OperationHeader;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class UafAuthenticationResponse {
    private OperationHeader header;
    private String fcParams;
    private ArrayList<AuthenticatorSignAssertion> assertions;
}