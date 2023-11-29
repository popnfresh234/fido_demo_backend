package com.example.apilogin.model.webauthn.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Fido2DoRegRestRequestBody {

	private RegPublicKeyCredential regPublicKeyCredential;

	public RegPublicKeyCredential getRegPublicKeyCredential() {
		return regPublicKeyCredential;
	}

	public void setRegPublicKeyCredential(RegPublicKeyCredential regPublicKeyCredential) {
		this.regPublicKeyCredential = regPublicKeyCredential;
	}

}
