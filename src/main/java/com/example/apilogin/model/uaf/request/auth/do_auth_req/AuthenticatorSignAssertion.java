package com.example.apilogin.model.uaf.request.auth.do_auth_req;

import com.example.apilogin.model.uaf.common.Extension;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class AuthenticatorSignAssertion {
    private String assertionScheme;
    private String assertion;
    private ArrayList<Extension> exts;
}
