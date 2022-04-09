package com.karson.sso.resource.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloController {
    @RequestMapping(value = "/",method = {RequestMethod.GET,RequestMethod.POST})
    public void index(HttpServletResponse response) throws IOException {
        response.sendRedirect("index.html");

    }
    @GetMapping("/getLoginUserName")
    public Map<String,Object> getLoginUserName(){
        Map<String,Object> result = new HashMap<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication)authentication;
        result.put("code",0);
        result.put("name",oAuth2Authentication.getName());
        return result;
       // return( (OAuth2AuthenticationDetails)oAuth2Authentication.getDetails()).getTokenValue();
        // return authentication.getName() + Arrays.toString(authentication.getAuthorities().toArray());

    }
}