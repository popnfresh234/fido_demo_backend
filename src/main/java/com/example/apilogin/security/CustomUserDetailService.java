package com.example.apilogin.security;

import com.example.apilogin.entities.RoleEntity;
import com.example.apilogin.entities.UserEntity;
import com.example.apilogin.service.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

@Log4j2
@Component
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        Optional<UserEntity> user = userRepository.findByAccount(account);
        if (user.isPresent()) {
            return UserPrincipal.builder()
                    .userId(user.get().getId())
                    .account(user.get().getAccount())
                    .authorities(buildAuthorities(user.get()))
                    .password(user.get().getPassword())
                    .build();
        }else {
            throw new DataAccessException("This user cannot be found"){};
        }
    }

    private ArrayList<SimpleGrantedAuthority> buildAuthorities(UserEntity user){
        Set<RoleEntity> roles = user.getRole();
        ArrayList<SimpleGrantedAuthority> arr = new ArrayList<>();
        roles.forEach((role) -> {
            arr.add(new SimpleGrantedAuthority(role.getRole()));
        });
        return arr;
    }
}
