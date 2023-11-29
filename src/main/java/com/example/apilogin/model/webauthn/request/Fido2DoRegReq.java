package com.example.apilogin.model.webauthn.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Fido2DoRegReq {

	private Fido2DoRegRestRequestBody body;
	private RestRequestHeader header;

	public Fido2DoRegRestRequestBody getBody() {
		return body;
	}

	public void setBody(Fido2DoRegRestRequestBody body) {
		this.body = body;
	}

	public RestRequestHeader getHeader() {
		return header;
	}

	public void setHeader(RestRequestHeader header) {
		this.header = header;
	}

}
