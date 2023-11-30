package com.example.apilogin.model.webauthn.request.reg.do_req;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)
@Getter
@Setter
public class RegPublicKeyCredential {

	private String id;
	private String rawId;
	private AuthenticatorAttestationResponse response;
	private String type;

}
