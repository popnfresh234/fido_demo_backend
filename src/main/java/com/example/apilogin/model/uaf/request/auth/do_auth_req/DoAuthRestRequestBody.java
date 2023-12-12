package com.example.apilogin.model.uaf.request.auth.do_auth_req;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class DoAuthRestRequestBody {
    private ArrayList<UafAuthenticationResponse> authResponses;
}
