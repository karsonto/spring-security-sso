package com.karson.sso.resource.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication)authentication;
     //   System.out.println(oAuth2Authentication);
        return( (OAuth2AuthenticationDetails)oAuth2Authentication.getDetails()).getTokenValue();
       // return authentication.getName() + Arrays.toString(authentication.getAuthorities().toArray());
    }
}