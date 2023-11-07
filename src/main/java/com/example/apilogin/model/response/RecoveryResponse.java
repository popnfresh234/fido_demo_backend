package com.example.apilogin.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecoveryResponse extends Response
{
    private final String account;
    private final String code;

    public RecoveryResponse(String message, String account, String code){
        super(message);
        this.account = account;
        this.code = code;
    }

}
