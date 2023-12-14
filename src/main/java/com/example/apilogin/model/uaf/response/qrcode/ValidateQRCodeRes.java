package com.example.apilogin.model.uaf.response.qrcode;

import com.example.apilogin.model.webauthn.response.common.RestResponseHeader;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidateQRCodeRes {
    private RestResponseHeader header;
    private ValidateQRCodeResponseBody body;
}
