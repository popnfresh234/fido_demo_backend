package com.example.apilogin.model.webauthn.response.auth.req_auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonInclude(Include.NON_NULL)
@Getter
@Setter
public class Fido2RequestAuthRestResponseBody {
	private List<PublicKeyCredentialDescriptor> allowCredentials;
	private String challenge;
	private Object extensions;
	private String rpId;
	private Long timeout;
	private String userVerification;
}
