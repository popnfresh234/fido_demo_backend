package com.example.apilogin.model.webauthn.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Fido2DoAuthReq {

	private Fido2DoAuthRestRequestBody body;
	private RestRequestHeader header;

	public Fido2DoAuthRestRequestBody getBody() {
		return body;
	}

	public void setBody(Fido2DoAuthRestRequestBody body) {
		this.body = body;
	}

	public RestRequestHeader getHeader() {
		return header;
	}

	public void setHeader(RestRequestHeader header) {
		this.header = header;
	}

}
