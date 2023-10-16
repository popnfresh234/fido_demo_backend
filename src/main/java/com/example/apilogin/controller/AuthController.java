package com.example.apilogin.controller;

import com.example.apilogin.entities.UserEntity;
import com.example.apilogin.model.LoginRequest;
import com.example.apilogin.model.LoginResponse;
import com.example.apilogin.security.JwtIssuer;
import com.example.apilogin.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class AuthController {
    private final JwtIssuer jwtIssuer;
    private final AuthenticationManager authenticationManager;

    @PostMapping("auth/login")
    public LoginResponse login(@RequestBody @Validated LoginRequest request) {

        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var principal = (UserPrincipal) authentication.getPrincipal();
        var roles = principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        var token = jwtIssuer.issue(principal.getUserId(), principal.getEmail(), roles);
        return new LoginResponse("Login Success", token);


    }
}
