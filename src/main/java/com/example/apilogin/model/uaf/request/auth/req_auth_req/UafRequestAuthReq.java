package com.example.apilogin.model.uaf.request.auth.req_auth_req;

import com.example.apilogin.model.webauthn.request.common.RestRequestHeader;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UafRequestAuthReq {
    private RestRequestHeader header;
    private RequestAuthRestRequestBody body;
}
