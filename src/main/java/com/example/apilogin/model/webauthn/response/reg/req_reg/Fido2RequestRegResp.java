package com.example.apilogin.model.webauthn.response.reg.req_reg;

import com.example.apilogin.model.webauthn.response.common.RestResponseHeader;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)
@Getter
@Setter
public class Fido2RequestRegResp {

	private Fido2RequestRegRestResponseBody body;
	private RestResponseHeader header;
}
