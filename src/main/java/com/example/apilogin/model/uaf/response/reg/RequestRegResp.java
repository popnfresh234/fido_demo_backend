package com.example.apilogin.model.uaf.response.reg;

import com.example.apilogin.model.webauthn.response.common.RestResponseHeader;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestRegResp {
    private RestResponseHeader header;
    private RequestRegRespResponseBody body;
}
