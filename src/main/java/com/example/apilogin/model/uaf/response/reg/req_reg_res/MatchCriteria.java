package com.example.apilogin.model.uaf.response.reg.req_reg_res;

import com.example.apilogin.model.uaf.common.Extension;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class MatchCriteria {
    private ArrayList aaid;
    private ArrayList vendorID;
    private ArrayList keyIDs;
    private Integer userVerification ;
    private Integer keyProtection;
    private Integer matcherProtection;
    private Integer attachmentHint;
    private Integer tcDisplay;
    private ArrayList authenticationAlgorithms;
    private ArrayList assertionSchemes;
    private ArrayList attestationTypes;
    private Integer authenticatorVersion;
    private ArrayList<Extension> exts;

}
