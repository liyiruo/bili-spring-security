package com.springsecurity.springsecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 在内存中配置密码，必须加密密码，不能使用明文密码
 */
@Configuration
@EnableWebSecurity//启用Spring security
@EnableGlobalMethodSecurity(prePostEnabled = true)//拦截@preAuthrize注解的配置
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder encoder;//这个东西很重要
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /*
         * 基于内存的方式构建两个账户
         * */
        auth
                .inMemoryAuthentication()
                .passwordEncoder(new BCryptPasswordEncoder())
                .withUser("admin")
                .password(new BCryptPasswordEncoder()
                        .encode("123"))
                .roles("admin");

        //两个构建账户的方式 看着不同 其实是一样的啊

        auth
                .inMemoryAuthentication()
                .passwordEncoder(encoder)
                .withUser("user")
                .password(encoder
                        .encode("123"))
                .roles("normal");
    }
}
