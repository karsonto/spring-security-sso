package com.karson.sso.auth.authentication;


import com.karson.sso.auth.utils.SecurityUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class JwtCustomerAccessTokenConverter extends DefaultAccessTokenConverter {
    private static final String DETAILS = "details";

    public JwtCustomerAccessTokenConverter() {
        super.setUserTokenConverter(new CustomerUserAuthenticationConverter());
    }

    private class CustomerUserAuthenticationConverter extends DefaultUserAuthenticationConverter {

        @Override
        public Map<String, ?> convertUserAuthentication(Authentication authentication) {
            Map<String, Object> response = new LinkedHashMap<String, Object>();
            response.put(USERNAME, authentication.getName());
            if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
                response.put(AUTHORITIES, AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
            }
            if (authentication instanceof UsernamePasswordAuthenticationToken) {
                UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
                if (token.getDetails() != null && (token.getDetails() instanceof MyWebAuthenticationDetails)) {
                    MyWebAuthenticationDetails authenticationDetails = (MyWebAuthenticationDetails) token.getDetails();
                    response.put(DETAILS, authenticationDetails.getExDetails());
                }
            }
            return response;
        }

        @Override
        public Authentication extractAuthentication(Map<String, ?> map) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = null;
            Authentication authentication = super.extractAuthentication(map);
            if(map.containsKey(DETAILS)){
               Object userDetails =  map.get(DETAILS);
               if(authentication!=null){
                   usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken)authentication;
                   MyWebAuthenticationDetails details = new MyWebAuthenticationDetails(SecurityUtil.getCurrentHttpRequest());
                   details.setExDetails((Map<String,Object>)userDetails);
                   usernamePasswordAuthenticationToken.setDetails(details);
               }
            }
            return usernamePasswordAuthenticationToken;
        }
    }

}