package com.springsecurity.springsecurity.premission.service.impl;

import com.springsecurity.springsecurity.premission.bean.UserInfo;
import com.springsecurity.springsecurity.premission.repository.UserInfoRepository;
import com.springsecurity.springsecurity.premission.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    UserInfoRepository userInfoRepository;

    @Override
    public UserInfo findByUsername(String username) {
        return userInfoRepository.findByUsername(username);
    }
}
