package com.example.apilogin.model.webauthn.request.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)
@Getter
@Setter
public class RestRequestHeader {

	private String appVersion;
	private String channelCode;
	private String deviceBrand;
	private String deviceType;
	private String deviceUuid;
	private String deviceVersion;
	private String userIp;

}
