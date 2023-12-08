package com.example.apilogin.model.uaf.response.reg;

import com.example.apilogin.model.uaf.common.Extension;
import com.example.apilogin.model.uaf.common.Version;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class OperationHeader {
    private Version upv;
    private String op;
    private String appID;
    private String serverData;
    private ArrayList<Extension> exts;
}
