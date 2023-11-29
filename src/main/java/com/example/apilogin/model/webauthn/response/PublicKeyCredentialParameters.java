package com.example.apilogin.model.webauthn.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PublicKeyCredentialParameters {

	private Long alg;
	private String type;

	public Long getAlg() {
		return alg;
	}

	public void setAlg(Long alg) {
		this.alg = alg;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
