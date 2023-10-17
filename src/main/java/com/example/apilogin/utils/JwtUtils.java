package com.example.apilogin.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

import java.util.Optional;

public class JwtUtils {
    public static Optional<String> extractTokenFromRequest(HttpServletRequest request){
        var token = request.getHeader("Authorization");
        return parseToken(token);
    }

    public static Optional<String> parseToken(String token){
        if(StringUtils.hasText(token) &&  token.startsWith("Bearer ")){
            return Optional.of(token.substring(7));
        }
        return Optional.empty();
    }
}
