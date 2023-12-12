package com.example.apilogin.model.uaf.request.reg.do_dereg_req;

import com.example.apilogin.model.webauthn.request.common.RestRequestHeader;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UafDoDeregReq {
    private RestRequestHeader header;
    private DoDeregRestRequestBody body;
}
