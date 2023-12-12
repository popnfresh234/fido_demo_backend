package com.example.apilogin.model.uaf.response.reg.do_dergeg_res;

import com.example.apilogin.model.webauthn.response.common.RestResponseHeader;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UafDoDeregRes {
    private RestResponseHeader header;
    private DoDeregRestResponseBody body;
}
