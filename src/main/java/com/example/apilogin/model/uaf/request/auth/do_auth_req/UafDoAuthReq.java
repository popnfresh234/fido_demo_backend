package com.example.apilogin.model.uaf.request.auth.do_auth_req;

import com.example.apilogin.model.webauthn.request.common.RestRequestHeader;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UafDoAuthReq {
    private RestRequestHeader header;
    private DoAuthRestRequestBody body;
}
