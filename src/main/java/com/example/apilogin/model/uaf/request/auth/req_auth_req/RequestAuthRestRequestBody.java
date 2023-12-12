package com.example.apilogin.model.uaf.request.auth.req_auth_req;

import com.example.apilogin.model.uaf.common.Transaction;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class RequestAuthRestRequestBody {
    private String username;
    private Transaction transaction;
    private String appID;
    private ArrayList extensionIdList;
}
