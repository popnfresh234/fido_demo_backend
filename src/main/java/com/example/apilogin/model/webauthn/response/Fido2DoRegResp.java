package com.example.apilogin.model.webauthn.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Fido2DoRegResp {

	private RestResponseHeader header;

	public RestResponseHeader getHeader() {
		return header;
	}

	public void setHeader(RestResponseHeader header) {
		this.header = header;
	}

}
