package com.example.apilogin.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.apilogin.entities.UserEntity;
import com.example.apilogin.security.JwtDecoder;
import com.example.apilogin.security.JwtToPrincipalConverter;
import com.example.apilogin.security.UserPrincipal;
import com.example.apilogin.service.UserRepository;
import com.example.apilogin.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping(path = "/user")
@RequiredArgsConstructor

public class UserController {
    private final JwtDecoder jwtDecoder;
    private final JwtToPrincipalConverter jwtToPrincipalConverter;
    @Autowired
    private UserRepository userRepository;

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<UserEntity> getAllUsers() {
        // This returns a JSON or XML with the users
        return userRepository.findAll();
    }

    @GetMapping(path = "/")

    public @ResponseBody UserEntity getUser(@RequestHeader (name="Authorization") String token, @RequestParam Integer id) {
        long currentId = -1;
        Optional<String> jwtToken = JwtUtils.parseToken(token);
        if(jwtToken.isPresent()){
            DecodedJWT test = jwtDecoder.decode(jwtToken.get());
            UserPrincipal principal= jwtToPrincipalConverter.convert(test);
            currentId = principal.getUserId();
        }

        if(currentId != id){
            throw new DataAccessException("Not Authorized") {
            };
        }

        Optional<UserEntity> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new DataAccessException("This user cannot be found") {
            };
        }
    }
}
