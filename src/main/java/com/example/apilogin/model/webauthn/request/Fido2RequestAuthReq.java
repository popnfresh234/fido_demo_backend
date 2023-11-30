package com.example.apilogin.model.webauthn.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Fido2RequestAuthReq {
	
	private Fido2RequestAuthRestRequestBody body;
	private RestRequestHeader header;
	
	public Fido2RequestAuthRestRequestBody getBody() {
		return body;
	}
	public void setBody(Fido2RequestAuthRestRequestBody body) {
		this.body = body;
	}
	public RestRequestHeader getHeader() {
		return header;
	}
	public void setHeader(RestRequestHeader header) {
		this.header = header;
	}
	

}
