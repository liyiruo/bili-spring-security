package com.springsecurity.springsecurity.premission.repository;

import com.springsecurity.springsecurity.premission.bean.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo,Long> {

    public UserInfo findByUsername(String username);
}
