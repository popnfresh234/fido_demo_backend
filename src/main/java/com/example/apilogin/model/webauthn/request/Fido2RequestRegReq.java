package com.example.apilogin.model.webauthn.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Fido2RequestRegReq {

	private Fido2RequestRegRestRequestBody body;
	private RestRequestHeader header;

	public Fido2RequestRegRestRequestBody getBody() {
		return body;
	}

	public void setBody(Fido2RequestRegRestRequestBody body) {
		this.body = body;
	}

	public RestRequestHeader getHeader() {
		return header;
	}

	public void setHeader(RestRequestHeader header) {
		this.header = header;
	}


	@Override
	public String toString() {
		return header.toString() + " " + body.toString();
	}
}
