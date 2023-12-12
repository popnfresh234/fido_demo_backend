package com.example.apilogin.model.uaf.response.auth.req_auth_res;

import com.example.apilogin.model.webauthn.response.common.RestResponseHeader;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UafRequestAuthRes {
    private RestResponseHeader header;
    private RequestAuthRestResponseBody body;
}
