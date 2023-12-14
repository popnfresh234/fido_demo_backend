package com.example.apilogin.model.uaf.request.pair;

import com.example.apilogin.model.uaf.common.Transaction;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class RequestPairAuthRestRequestBody {
    private String paircode;
    private String username;
    private Transaction transaction;
    private String appID;
    private ArrayList<String> extensionIdList;
}
