package com.example.apilogin.model.uaf.response.reg.do_dergeg_res;

import com.example.apilogin.model.uaf.common.DeregisterAuthenticator;
import com.example.apilogin.model.uaf.common.OperationHeader;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class DoDeregRestResponseBody {
    private OperationHeader header;
    private ArrayList<DeregisterAuthenticator> authenticators;
}
