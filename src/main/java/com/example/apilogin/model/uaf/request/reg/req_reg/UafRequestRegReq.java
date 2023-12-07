package com.example.apilogin.model.uaf.request.reg.req_reg;

import com.example.apilogin.model.webauthn.request.common.RestRequestHeader;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UafRequestRegReq {
    RestRequestHeader header;
    UafRequestRegRestRequestBody body;
}
