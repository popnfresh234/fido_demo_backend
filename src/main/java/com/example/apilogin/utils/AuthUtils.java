package com.example.apilogin.utils;

import com.example.apilogin.security.JwtToPrincipalConverter;
import com.example.apilogin.security.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class AuthUtils {

    public static UserPrincipal getPrincipal(){
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        Jwt token = (Jwt) authentication.getPrincipal();
        JwtToPrincipalConverter jwtToPrincipalConverter = new JwtToPrincipalConverter();
        return jwtToPrincipalConverter.convert(token);
    }
}
