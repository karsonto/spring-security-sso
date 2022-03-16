package com.karson.sso.auth.authentication;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.HashMap;
import java.util.Map;

public class MyWebAuthenticationDetails extends WebAuthenticationDetails {
    private Map<String,Object> exDetails = new HashMap<>();

    public MyWebAuthenticationDetails(javax.servlet.http.HttpServletRequest request) {
        super(request);
    }

    public Map<String, Object> getExDetails() {
        return exDetails;
    }

    public void setExDetails(Map<String, Object> exDetails) {
        this.exDetails = exDetails;
    }
}
