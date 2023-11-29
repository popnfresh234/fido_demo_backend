package com.example.apilogin.model.webauthn.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class AuthenticatorAssertionResponse {

	private String authenticatorData;
	private String clientDataJSON;
	private String signature;
	private String userHandle;

	public String getAuthenticatorData() {
		return authenticatorData;
	}

	public void setAuthenticatorData(String authenticatorData) {
		this.authenticatorData = authenticatorData;
	}

	public String getClientDataJSON() {
		return clientDataJSON;
	}

	public void setClientDataJSON(String clientDataJSON) {
		this.clientDataJSON = clientDataJSON;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getUserHandle() {
		return userHandle;
	}

	public void setUserHandle(String userHandle) {
		this.userHandle = userHandle;
	}

}
