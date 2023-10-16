package com.example.apilogin.security;

import org.apache.catalina.User;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class UserPrincipalAuthToken extends AbstractAuthenticationToken {

    private final UserPrincipal principal;

    public UserPrincipalAuthToken(UserPrincipal principal){
        super(principal.getAuthorities());
        this.principal = principal;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public UserPrincipal getPrincipal() {
        return principal;
    }
}
