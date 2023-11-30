package com.example.apilogin.model.webauthn.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.List;

@JsonInclude(Include.NON_NULL)
public class Fido2RequestAuthRestResponseBody {

	private List<PublicKeyCredentialDescriptor> allowCredentials;
	private String challenge;
	private Object extensions;
	private String rpId;
	private Long timeout;
	private String userVerification;

	public List<PublicKeyCredentialDescriptor> getAllowCredentials() {
		return allowCredentials;
	}

	public void setAllowCredentials(List<PublicKeyCredentialDescriptor> allowCredentials) {
		this.allowCredentials = allowCredentials;
	}

	public String getChallenge() {
		return challenge;
	}

	public void setChallenge(String challenge) {
		this.challenge = challenge;
	}

	public Object getExtensions() {
		return extensions;
	}

	public void setExtensions(Object extensions) {
		this.extensions = extensions;
	}

	public String getRpId() {
		return rpId;
	}

	public void setRpId(String rpId) {
		this.rpId = rpId;
	}

	public Long getTimeout() {
		return timeout;
	}

	public void setTimeout(Long timeout) {
		this.timeout = timeout;
	}

	public String getUserVerification() {
		return userVerification;
	}

	public void setUserVerification(String userVerification) {
		this.userVerification = userVerification;
	}

}
