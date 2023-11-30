package com.example.apilogin.model.webauthn.response.auth.req_auth;

import com.example.apilogin.model.webauthn.response.common.RestResponseHeader;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)
@Getter
@Setter
public class Fido2RequestAuthResp {
	private Fido2RequestAuthRestResponseBody body;
	private RestResponseHeader header;
}
