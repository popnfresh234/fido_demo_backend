package com.example.apilogin.model.webauthn.response.reg.req_reg;

import com.example.apilogin.model.webauthn.response.auth.req_auth.PublicKeyCredentialDescriptor;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonInclude(Include.NON_NULL)
@Getter
@Setter
public class Fido2RequestRegRestResponseBody {

	private String attestation;
	private AuthenticatorSelectionCriteria authenticatorSelection;
	private String challenge;
	private List<PublicKeyCredentialDescriptor> excludeCredentials;
	private Object extensions;
	private List<PublicKeyCredentialParameters> pubKeyCredParams;
	private PublicKeyCredentialRpEntity rp;
	private Long timeout;
	private PublicKeyCredentialUserEntity user;

}
