package com.example.apilogin.model.webauthn.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Fido2RequestRegResp {

	private Fido2RequestRegRestResponseBody body;
	private RestResponseHeader header;

	public Fido2RequestRegRestResponseBody getBody() {
		return body;
	}

	public void setBody(Fido2RequestRegRestResponseBody body) {
		this.body = body;
	}

	public RestResponseHeader getHeader() {
		return header;
	}

	public void setHeader(RestResponseHeader header) {
		this.header = header;
	}

}
