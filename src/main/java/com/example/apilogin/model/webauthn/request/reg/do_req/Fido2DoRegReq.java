package com.example.apilogin.model.webauthn.request.reg.do_req;

import com.example.apilogin.model.webauthn.request.common.RestRequestHeader;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)
@Getter
@Setter
public class Fido2DoRegReq {

	private Fido2DoRegRestRequestBody body;
	private RestRequestHeader header;


}
