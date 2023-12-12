package com.example.apilogin.model.uaf.request.reg.do_dereg_req;

import com.example.apilogin.model.uaf.common.DeregisterAuthenticator;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class DoDeregRestRequestBody {
    private String username;
    private ArrayList<DeregisterAuthenticator> authenticators;
    private String appID;
}
