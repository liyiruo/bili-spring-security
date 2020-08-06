package com.springsecurity.springsecurity.premission.service;

import com.springsecurity.springsecurity.premission.bean.UserInfo;

public interface UserInfoService {
    public UserInfo findByUsername(String username);
}
