package com.example.apilogin.model.uaf.request.qr_code;

import com.example.apilogin.model.webauthn.request.common.RestRequestHeader;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class RequestQRCodeReq {
    private RestRequestHeader header;
    private RequestQrCodeRequestBody body;
}
