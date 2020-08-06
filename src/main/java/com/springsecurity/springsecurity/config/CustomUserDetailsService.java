package com.springsecurity.springsecurity.config;

import com.springsecurity.springsecurity.premission.bean.UserInfo;
import com.springsecurity.springsecurity.premission.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserInfoService service;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        UserInfo userInfo = service.findByUsername(s);
        if (userInfo == null) {
            throw new UsernameNotFoundException("not found : " + s);
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + userInfo.getRole().name()));
        User userDetails = new User(userInfo.getUsername(), encoder.encode(userInfo.getPassword()), authorities);

        return userDetails;
    }
}
