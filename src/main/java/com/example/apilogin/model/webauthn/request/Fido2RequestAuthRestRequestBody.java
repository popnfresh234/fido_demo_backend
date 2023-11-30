package com.example.apilogin.model.webauthn.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Fido2RequestAuthRestRequestBody {

	private Object extensions;
	private String origin;
	private String rpId;
	private String userVerification;
	private String username;

	public Object getExtensions() {
		return extensions;
	}

	public void setExtensions(Object extensions) {
		this.extensions = extensions;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getRpId() {
		return rpId;
	}

	public void setRpId(String rpId) {
		this.rpId = rpId;
	}

	public String getUserVerification() {
		return userVerification;
	}

	public void setUserVerification(String userVerification) {
		this.userVerification = userVerification;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
