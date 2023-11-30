package com.example.apilogin.model.webauthn.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class AuthPublicKeyCredential {

	private String id;
	private String rawId;
	private AuthenticatorAssertionResponse response;
	private String type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRawId() {
		return rawId;
	}

	public void setRawId(String rawId) {
		this.rawId = rawId;
	}

	public AuthenticatorAssertionResponse getResponse() {
		return response;
	}

	public void setResponse(AuthenticatorAssertionResponse response) {
		this.response = response;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
