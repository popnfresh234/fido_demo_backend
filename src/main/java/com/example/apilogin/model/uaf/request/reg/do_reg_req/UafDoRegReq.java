package com.example.apilogin.model.uaf.request.reg.do_reg_req;

import com.example.apilogin.model.webauthn.request.common.RestRequestHeader;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UafDoRegReq {
    RestRequestHeader header;
    DoRegRestRequestBody body;
}
