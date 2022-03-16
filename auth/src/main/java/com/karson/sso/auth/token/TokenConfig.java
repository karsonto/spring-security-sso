package com.karson.sso.auth.token;

import com.karson.sso.auth.authentication.JwtCustomerAccessTokenConverter;
import com.karson.sso.auth.authentication.MyWebAuthenticationDetailsSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
//@Order(1)
public class TokenConfig {

    @Bean
    public TokenStore jwtTokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        //指定signkey
        converter.setSigningKey("karson520fun");
        converter.setAccessTokenConverter(new JwtCustomerAccessTokenConverter());
        return converter;
    }
    @Bean
    public MyWebAuthenticationDetailsSource myWebAuthenticationDetailsSource(){
        return new MyWebAuthenticationDetailsSource();
    }
}
