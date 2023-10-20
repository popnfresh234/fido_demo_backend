package com.example.apilogin.security;

import com.example.apilogin.entities.UserEntity;
import com.example.apilogin.service.UserRepository;
import com.example.apilogin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> user = userRepository.findByEmail(username);
        if (user.isPresent()) {
            return UserPrincipal.builder()
                    .userId(user.get().getId())
                    .email(user.get().getEmail())
                    .authorities(List.of(new SimpleGrantedAuthority(user.get().getRole())))
                    .password(user.get().getPassword())
                    .build();
        }else {
            throw new DataAccessException("This user cannot be found"){};
        }
    }
}
