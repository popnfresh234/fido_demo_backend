package com.example.apilogin.model.webauthn.response.reg.do_reg;

import com.example.apilogin.model.webauthn.response.common.RestResponseHeader;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)
@Getter
@Setter
public class Fido2DoRegResp {
	private RestResponseHeader header;
}
