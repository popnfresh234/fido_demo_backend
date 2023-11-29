package com.example.apilogin.model.webauthn.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.List;

@JsonInclude(Include.NON_NULL)
public class PublicKeyCredentialDescriptor {

	private String id;
	private List<String> transports;
	private String type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<String> getTransports() {
		return transports;
	}

	public void setTransports(List<String> transports) {
		this.transports = transports;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
