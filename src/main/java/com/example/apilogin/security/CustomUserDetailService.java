package com.example.apilogin.security;

import com.example.apilogin.service.UserRepository;
import com.example.apilogin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

//        var user = userService.findByEmail(username).orElseThrow();
//        return UserPrincipal.builder()
//                .userId(user.getId())
//                .email(user.getEmail())
//                .authorities(List.of(new SimpleGrantedAuthority(user.getRole())))
//                .password(user.getPassword())
//                .build();
        try {
            var user = userRepository.findByEmail(username);
            return UserPrincipal.builder()
                    .userId(user.getId())
                    .email(user.getEmail())
                    .authorities(List.of(new SimpleGrantedAuthority(user.getRole())))
                    .password(user.getPassword())
                    .build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
