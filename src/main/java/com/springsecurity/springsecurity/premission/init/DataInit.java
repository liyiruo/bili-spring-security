package com.springsecurity.springsecurity.premission.init;

import com.springsecurity.springsecurity.premission.bean.UserInfo;
import com.springsecurity.springsecurity.premission.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class DataInit {
    @Autowired
    private UserInfoRepository userInfoRepository;
    @PostConstruct
    public void dataInit() {
        UserInfo user = new UserInfo();
        user.setUsername("user");
        user.setPassword("123");
        user.setRole(UserInfo.Role.normal);
        userInfoRepository.save(user);


        UserInfo admin = new UserInfo();
        admin.setUsername("admin");
        admin.setPassword("123");
        admin.setRole(UserInfo.Role.admin);
        userInfoRepository.save(admin);
    }

}
