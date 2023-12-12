package com.example.apilogin.model.response;

import com.example.apilogin.model.uaf.response.reg.req_reg_res.UafReqRegRes;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UafRequestRegResponse extends Response
{
    private UafReqRegRes requestRegResp;
    public UafRequestRegResponse(String message, UafReqRegRes requestRegResp){
        super(message);
        this.requestRegResp = requestRegResp;
    }

}
