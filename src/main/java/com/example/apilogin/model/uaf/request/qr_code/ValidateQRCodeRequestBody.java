package com.example.apilogin.model.uaf.request.qr_code;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidateQRCodeRequestBody {
    String paircode;
    String appId;
}
