package com.example.apilogin.model.webauthn.request.auth.req_do_auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)
@Getter
@Setter
public class AuthPublicKeyCredential {

	private String id;
	private String rawId;
	private AuthenticatorAssertionResponse response;
	private String type;

}
