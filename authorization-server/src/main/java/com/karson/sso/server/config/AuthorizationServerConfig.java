package com.karson.sso.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/**
 * @ClassName AuthorizationServerConfig
 * @Description TODO
 * @Author Karson
 * @Date 2022-03-12 13:39
 * @Version 1.0
 **/
@Configuration

//开启oauth2,auth server模式
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    JwtAccessTokenConverter jwtAccessTokenConverter;
    @Autowired
    private TokenStore tokenStore;

//    @Autowired
//    private JwtAccessTokenConverter accessTokenConverter;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;

    //密码模式才需要配置,认证管理器
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;




//    @Bean
//    public ClientDetailsService clientDetailsService(DataSource dataSource) {
//        ClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
//        ((JdbcClientDetailsService) clientDetailsService).setPasswordEncoder(passwordEncoder);
//        return clientDetailsService;
//    }
    //配置客户端
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
       // clients.withClientDetails(clientDetailsService);


         clients.inMemory()
                //client的id和密码
                .withClient("c1")
                .secret(passwordEncoder.encode("c1"))

                //给client一个id,这个在client的配置里要用的
                .resourceIds("resource1")

                //允许的申请token的方式,测试用例在test项目里都有.
                //authorization_code授权码模式,这个是标准模式
                //implicit简单模式,这个主要是给无后台的纯前端项目用的
                //password密码模式,直接拿用户的账号密码授权,不安全
                //client_credentials客户端模式,用clientid和密码授权,和用户无关的授权方式
                //refresh_token使用有效的refresh_token去重新生成一个token,之前的会失效
                .authorizedGrantTypes("authorization_code", "password", "client_credentials", "implicit", "refresh_token")

                //授权的范围,每个resource会设置自己的范围.
                .scopes("all")

                //这个是设置要不要弹出确认授权页面的.
                .autoApprove(true)


                //这个相当于是client的域名,重定向给code的时候会跳转这个域名
                .redirectUris("http://localhost:8081/login");

                /*.and()

                .withClient("client2")
                .secret(passwordEncoder.encode("123123"))
                .resourceIds("resource1")
                .authorizedGrantTypes("authorization_code", "password", "client_credentials", "implicit", "refresh_token")
                .scopes("all")
                .autoApprove(false)
                .redirectUris("http://www.qq.com");*/
    }




    //配置token管理服务
//    @Bean
//    public AuthorizationServerTokenServices tokenServices() {
//        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
//        defaultTokenServices.setClientDetailsService(clientDetailsService);
//        defaultTokenServices.setSupportRefreshToken(true);
//        defaultTokenServices.setTokenStore(tokenStore);
////        // 令牌增强
////        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
////        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(accessTokenConverter));
////        defaultTokenServices.setTokenEnhancer(tokenEnhancerChain);
//        //配置token的存储方法
//        defaultTokenServices.setAccessTokenValiditySeconds(7200);
//        defaultTokenServices.setRefreshTokenValiditySeconds(259200);
//        return defaultTokenServices;
//    }




    //把上面的各个组件组合在一起
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore).accessTokenConverter(jwtAccessTokenConverter);
        endpoints.authenticationManager(authenticationManager)//认证管理器，密碼模式才用到
                .authorizationCodeServices(authorizationCodeServices)//授权码管理，授權碼模式才用到
               // .tokenServices(tokenServices())//token管理，所有模式必須配置這個
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);
    }

    //配置哪些接口可以被访问
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .tokenKeyAccess("permitAll()")///oauth/token_key公开
                .checkTokenAccess("permitAll()")///oauth/check_token公开
                .allowFormAuthenticationForClients();//允许表单认证
    }

   /* @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new InMemoryAuthorizationCodeServices();
    }*/



    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
       // return new JdbcAuthorizationCodeServices(dataSource);  // 设置授权码模式的授权码如何存取
        return new InMemoryAuthorizationCodeServices();
    }


}