package com.example.apilogin.model.uaf.request.reg.do_reg_req;

import com.example.apilogin.model.uaf.common.Extension;
import com.example.apilogin.model.uaf.common.OperationHeader;
import com.example.apilogin.model.uaf.common.Version;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class UafRegistrationResponse {
    private OperationHeader header;
    private String fcParams;
    private ArrayList<AuthenticatorRegistrationAssertion> assertions;
}
