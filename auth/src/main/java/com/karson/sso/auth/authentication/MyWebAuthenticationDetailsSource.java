package com.karson.sso.auth.authentication;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;


public class MyWebAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest,MyWebAuthenticationDetails> {
    @Override
    public MyWebAuthenticationDetails buildDetails(HttpServletRequest context) {
        MyWebAuthenticationDetails myWebAuthenticationDetails = new MyWebAuthenticationDetails(context);
        myWebAuthenticationDetails.getExDetails().put("karson","karson");
        return  myWebAuthenticationDetails;
    }
}