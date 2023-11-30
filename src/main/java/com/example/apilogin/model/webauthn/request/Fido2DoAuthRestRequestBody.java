package com.example.apilogin.model.webauthn.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Fido2DoAuthRestRequestBody {

	private AuthPublicKeyCredential publicKeyCredential;
	private String tokenBindingId;

	public AuthPublicKeyCredential getPublicKeyCredential() {
		return publicKeyCredential;
	}

	public void setPublicKeyCredential(AuthPublicKeyCredential publicKeyCredential) {
		this.publicKeyCredential = publicKeyCredential;
	}

	public String getTokenBindingId() {
		return tokenBindingId;
	}

	public void setTokenBindingId(String tokenBindingId) {
		this.tokenBindingId = tokenBindingId;
	}

}
