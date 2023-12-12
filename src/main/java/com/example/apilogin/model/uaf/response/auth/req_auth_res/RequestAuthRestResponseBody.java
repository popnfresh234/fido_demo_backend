package com.example.apilogin.model.uaf.response.auth.req_auth_res;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class RequestAuthRestResponseBody {
    private ArrayList<UAFAuthenticationRequest> authRequests;
}
