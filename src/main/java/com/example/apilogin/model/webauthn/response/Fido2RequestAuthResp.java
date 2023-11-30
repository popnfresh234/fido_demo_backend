package com.example.apilogin.model.webauthn.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Fido2RequestAuthResp {

	private Fido2RequestAuthRestResponseBody body;
	private RestResponseHeader header;

	public Fido2RequestAuthRestResponseBody getBody() {
		return body;
	}

	public void setBody(Fido2RequestAuthRestResponseBody body) {
		this.body = body;
	}

	public RestResponseHeader getHeader() {
		return header;
	}

	public void setHeader(RestResponseHeader header) {
		this.header = header;
	}

}
