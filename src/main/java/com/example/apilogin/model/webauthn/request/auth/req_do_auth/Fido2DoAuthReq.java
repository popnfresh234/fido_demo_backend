package com.example.apilogin.model.webauthn.request.auth.req_do_auth;

import com.example.apilogin.model.webauthn.request.common.RestRequestHeader;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)
@Getter
@Setter
public class Fido2DoAuthReq {
	private Fido2DoAuthRestRequestBody body;
	private RestRequestHeader header;
}
