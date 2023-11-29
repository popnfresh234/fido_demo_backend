package com.example.apilogin.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JwtToPrincipalConverter {
    public UserPrincipal convert(Jwt jwt){
        return UserPrincipal.builder()
                .userId(Long.valueOf(jwt.getSubject()))
                .account(jwt.getClaim("account"))
                .email(jwt.getClaim("email"))
                .name(jwt.getClaim("name"))
                .authorities(extractAuthoritiesFromClaim(jwt))
                .build();
    }

    private List<SimpleGrantedAuthority> extractAuthoritiesFromClaim(Jwt jwt){
        ArrayList<String> authorityStrings =  jwt.getClaim("authorities");
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorityStrings.forEach((auth) -> {
            authorities.add(new SimpleGrantedAuthority(auth));
        });
        return authorities;
    }
}
