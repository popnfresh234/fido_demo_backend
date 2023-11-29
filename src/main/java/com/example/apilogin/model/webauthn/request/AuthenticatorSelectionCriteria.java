package com.example.apilogin.model.webauthn.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class AuthenticatorSelectionCriteria {

	private String authenticatorAttachment;
	private Boolean requireResidentKey;
	private String auserVerification;

	public String getAuthenticatorAttachment() {
		return authenticatorAttachment;
	}

	public void setAuthenticatorAttachment(String authenticatorAttachment) {
		this.authenticatorAttachment = authenticatorAttachment;
	}

	public Boolean getRequireResidentKey() {
		return requireResidentKey;
	}

	public void setRequireResidentKey(Boolean requireResidentKey) {
		this.requireResidentKey = requireResidentKey;
	}

	public String getAuserVerification() {
		return auserVerification;
	}

	public void setAuserVerification(String auserVerification) {
		this.auserVerification = auserVerification;
	}

}
