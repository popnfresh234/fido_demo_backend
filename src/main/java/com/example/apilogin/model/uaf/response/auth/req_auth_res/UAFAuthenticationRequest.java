package com.example.apilogin.model.uaf.response.auth.req_auth_res;

import com.example.apilogin.model.uaf.common.OperationHeader;
import com.example.apilogin.model.uaf.common.Transaction;
import com.example.apilogin.model.uaf.response.reg.req_reg_res.PolicyVo;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class UAFAuthenticationRequest {
    private OperationHeader header;
    private String challenge;
    private ArrayList<Transaction> transaction;
    private PolicyVo policy;
}
