package com.karson.sso.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
@Component
public class MyPreserveHostHeaderGatewayFilterFactory  extends AbstractGatewayFilterFactory<MyPreserveHostHeaderGatewayFilterFactory.Config> {
    public MyPreserveHostHeaderGatewayFilterFactory() {
        super(Config.class);
    }
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("url");
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                ServerHttpResponse response = exchange.getResponse();
                String configUrl = config.getUrl();
                if(response.getStatusCode().value()== 302){
                    URI location = response.getHeaders().getLocation();
                    String uri = location.toString();
                    if(location.getPath().equals("/")){
                        uri = uri.substring(0,uri.length()-1) + configUrl;
                    }else{
                        uri = uri.replace(location.getPath(),configUrl+location.getPath());
                    }

                    try {
                        response.getHeaders().setLocation(new URI(uri));
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }));
        };

    }

    public static class Config {
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
