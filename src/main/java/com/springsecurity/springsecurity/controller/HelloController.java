package com.springsecurity.springsecurity.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping(value = "/hello")
    public String hello() {
        return "HelloWorld";
    }

    @GetMapping(value = "/helloAdmin")
    @PreAuthorize("hasAnyRole('admin')")
    public String helloAdmin() {
        return "HelloWorld，helloAdmin";
    }
    @PreAuthorize("hasAnyRole('normal','admin')")
    @GetMapping(value = "/helloUser")
    public String helloUser() {
        return "HelloWorld，helloUser";
    }

}
