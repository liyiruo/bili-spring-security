package com.springsecurity.springsecurity.premission.bean;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class UserInfo {
    @Id
    @GeneratedValue
    private Long uid;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role {
        admin, normal
    }
}
