package com.example.apilogin.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSingleton {
    private static UserSingleton INSTANCE;
    private String username = "";

    private UserSingleton(){}

    public static UserSingleton getInstance(){
        if (INSTANCE == null){
            INSTANCE = new UserSingleton();
        }
        return INSTANCE;
    }
}
