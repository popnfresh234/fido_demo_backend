package com.example.apilogin.model.uaf.response.reg.req_reg_res;

import com.example.apilogin.model.uaf.common.OperationHeader;
import com.example.apilogin.model.uaf.common.PolicyVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UAFRegistrationRequest {
    OperationHeader header;
    String challenge;
    String username;
    PolicyVo policy;
}
