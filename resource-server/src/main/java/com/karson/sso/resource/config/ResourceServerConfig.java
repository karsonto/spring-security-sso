package com.karson.sso.resource.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

//@Configuration
//@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Autowired
    JwtAccessTokenConverter jwtAccessTokenConverter;
    @Autowired
    private TokenStore tokenStore;

//    @Bean
//    RemoteTokenServices tokenServices() {
//        RemoteTokenServices services = new RemoteTokenServices();
//        services.setCheckTokenEndpointUrl("http://localhost:8080/oauth/check_token");
//        services.setClientId("c1");
//        services.setClientSecret("c1");
//        return services;
//    }
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("resource1").tokenStore(tokenStore);
    }


}