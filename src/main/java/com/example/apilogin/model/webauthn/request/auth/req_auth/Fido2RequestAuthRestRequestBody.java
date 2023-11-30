package com.example.apilogin.model.webauthn.request.auth.req_auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)
@Getter
@Setter
public class Fido2RequestAuthRestRequestBody {

	private Object extensions;
	private String origin;
	private String rpId;
	private String userVerification;
	private String username;
}
