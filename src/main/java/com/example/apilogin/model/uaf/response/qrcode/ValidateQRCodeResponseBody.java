package com.example.apilogin.model.uaf.response.qrcode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidateQRCodeResponseBody {
    private boolean validate;
    private String username;
    private String validationStatus;
}
