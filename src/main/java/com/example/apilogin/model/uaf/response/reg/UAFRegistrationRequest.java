package com.example.apilogin.model.uaf.response.reg;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UAFRegistrationRequest {
    OperationHeader header;
    String challenge;
    String username;
    PolicyVo policy;
}
