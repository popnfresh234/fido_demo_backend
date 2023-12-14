package com.example.apilogin.model.uaf.request.pair;

import com.example.apilogin.model.webauthn.request.common.RestRequestHeader;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UafRequestPairAuthReq {
    private RestRequestHeader header;
    private RequestPairAuthRestRequestBody body;
}
