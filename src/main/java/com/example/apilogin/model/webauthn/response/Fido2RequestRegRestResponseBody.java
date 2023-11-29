package com.example.apilogin.model.webauthn.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.List;

@JsonInclude(Include.NON_NULL)
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

	public String getAttestation() {
		return attestation;
	}

	public void setAttestation(String attestation) {
		this.attestation = attestation;
	}

	public AuthenticatorSelectionCriteria getAuthenticatorSelection() {
		return authenticatorSelection;
	}

	public void setAuthenticatorSelection(AuthenticatorSelectionCriteria authenticatorSelection) {
		this.authenticatorSelection = authenticatorSelection;
	}

	public String getChallenge() {
		return challenge;
	}

	public void setChallenge(String challenge) {
		this.challenge = challenge;
	}

	public List<PublicKeyCredentialDescriptor> getExcludeCredentials() {
		return excludeCredentials;
	}

	public void setExcludeCredentials(List<PublicKeyCredentialDescriptor> excludeCredentials) {
		this.excludeCredentials = excludeCredentials;
	}

	public Object getExtensions() {
		return extensions;
	}

	public void setExtensions(Object extensions) {
		this.extensions = extensions;
	}

	public List<PublicKeyCredentialParameters> getPubKeyCredParams() {
		return pubKeyCredParams;
	}

	public void setPubKeyCredParams(List<PublicKeyCredentialParameters> pubKeyCredParams) {
		this.pubKeyCredParams = pubKeyCredParams;
	}

	public PublicKeyCredentialRpEntity getRp() {
		return rp;
	}

	public void setRp(PublicKeyCredentialRpEntity rp) {
		this.rp = rp;
	}

	public Long getTimeout() {
		return timeout;
	}

	public void setTimeout(Long timeout) {
		this.timeout = timeout;
	}

	public PublicKeyCredentialUserEntity getUser() {
		return user;
	}

	public void setUser(PublicKeyCredentialUserEntity user) {
		this.user = user;
	}

}
