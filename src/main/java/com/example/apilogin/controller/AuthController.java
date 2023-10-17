package com.example.apilogin.controller;

import com.example.apilogin.entities.UserEntity;
import com.example.apilogin.model.LoginRequest;
import com.example.apilogin.model.LoginResponse;
import com.example.apilogin.model.Response;
import com.example.apilogin.model.SignupResponse;
import com.example.apilogin.security.JwtIssuer;
import com.example.apilogin.security.UserPrincipal;
import com.example.apilogin.service.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(path="/auth")
public class AuthController {
    private final JwtIssuer jwtIssuer;
    private final AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
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


    @PostMapping("/signup")
    public Response signup(@RequestParam String name,
                           @RequestParam String email,
                           @RequestParam String password) {
        var user = new UserEntity();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("ROLE_ADMIN");
        user.setExtraInfo(("My nice admin"));
        if(userRepository.findByEmail(email)!= null){
            throw new DataAccessException("This user already exists") {
            };
        }else {
            userRepository.save(user);
            return new SignupResponse("Saved");
        }
    }
}
