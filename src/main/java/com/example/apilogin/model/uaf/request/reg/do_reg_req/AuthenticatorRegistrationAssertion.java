package com.example.apilogin.model.uaf.request.reg.do_reg_req;

import com.example.apilogin.model.uaf.common.DisplayPNGCharacteristicsDescriptor;
import com.example.apilogin.model.uaf.common.Extension;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class AuthenticatorRegistrationAssertion {
    private String assertionScheme;
    private String assertion;
    private ArrayList<Extension> exts;
    private ArrayList<DisplayPNGCharacteristicsDescriptor> tcDisplayPNGCharacteristics;
}
