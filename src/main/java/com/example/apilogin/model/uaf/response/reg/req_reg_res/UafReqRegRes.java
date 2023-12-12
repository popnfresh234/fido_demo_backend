package com.example.apilogin.model.uaf.response.reg.req_reg_res;

import com.example.apilogin.model.webauthn.response.common.RestResponseHeader;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UafReqRegRes {
    private RestResponseHeader header;
    private RequestRegRespResponseBody body;
}
