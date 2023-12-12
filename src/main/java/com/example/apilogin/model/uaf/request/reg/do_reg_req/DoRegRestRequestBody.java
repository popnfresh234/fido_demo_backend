package com.example.apilogin.model.uaf.request.reg.do_reg_req;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class DoRegRestRequestBody {
    ArrayList<UafRegistrationResponse> regResponses;
}
