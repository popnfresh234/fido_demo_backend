package com.example.apilogin.model.uaf.response.reg.do_reg_res;

import com.example.apilogin.model.webauthn.response.common.RestResponseHeader;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UafDoRegRes {
    private RestResponseHeader header;
    private DoRegRestResponseBody body;
}
