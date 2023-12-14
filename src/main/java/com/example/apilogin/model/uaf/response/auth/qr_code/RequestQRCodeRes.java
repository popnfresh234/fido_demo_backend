package com.example.apilogin.model.uaf.response.auth.qr_code;

import com.example.apilogin.model.webauthn.response.common.RestResponseHeader;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestQRCodeRes {
    private RestResponseHeader header;
    private RequestQRCodeResponseBody body;
}
