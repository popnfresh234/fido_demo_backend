package com.example.apilogin.security.filters;

import com.example.apilogin.security.JwtToPrincipalConverter;
import com.example.apilogin.security.UserPrincipal;
import com.example.apilogin.utils.NetworkUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Log4j2
@Component
public class RoleFilter extends OncePerRequestFilter {
    private final JwtToPrincipalConverter jwtToPrincipalConverter;
    private final RequestMatcher matcher = new AntPathRequestMatcher("/news/delete/**");


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        log.info("Role Filter");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt token = (Jwt) authentication.getPrincipal();
        UserPrincipal principal = jwtToPrincipalConverter.convert(token);
        boolean admin = false;

        for (GrantedAuthority auth : principal.getAuthorities()) {
            if (auth.getAuthority().equals("ROLE_ADMIN")) {
                admin = true;
            }
        }

        if (!admin) {
            NetworkUtils.buildErrorResponse(response);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        RequestMatcher matcher = new NegatedRequestMatcher(this.matcher);
        return matcher.matches(request);
    }
}
