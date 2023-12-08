package com.example.apilogin.model.response;

import com.example.apilogin.model.uaf.response.reg.RequestRegResp;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UafRequestRegResponse extends Response
{
    private RequestRegResp requestRegResp;
    public UafRequestRegResponse(String message, RequestRegResp requestRegResp){
        super(message);
        this.requestRegResp = requestRegResp;
    }

}
