package com.example.apilogin.model.uaf.common;

import com.example.apilogin.model.uaf.response.reg.req_reg_res.MatchCriteria;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class PolicyVo {
    private ArrayList accepted;
    private ArrayList<MatchCriteria> disallowed;
}
