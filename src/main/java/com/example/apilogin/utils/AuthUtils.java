package com.example.apilogin.utils;

import com.example.apilogin.security.JwtToPrincipalConverter;
import com.example.apilogin.security.UserPrincipal;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

@Log4j2
public class AuthUtils {

    public static UserPrincipal getPrincipal(){
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        try {
            Jwt token = (Jwt) authentication.getPrincipal();
            JwtToPrincipalConverter jwtToPrincipalConverter = new JwtToPrincipalConverter();
            return jwtToPrincipalConverter.convert(token);
        } catch(Exception e){
            log.error("Problem parsing principal");
            log.error(authentication.getPrincipal());
            throw e;
        }
    }
}

