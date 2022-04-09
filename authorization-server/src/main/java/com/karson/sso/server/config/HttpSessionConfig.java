package com.karson.sso.server.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
    public class HttpSessionConfig {

        private static final Map<String, HttpSession> sessions = new ConcurrentHashMap<>();

        private static final Map<String, String> tokenSessionRef = new ConcurrentHashMap<>();

        @Bean
        public HttpSessionListener httpSessionListener() {
            return new HttpSessionListener() {
                @Override
                public void sessionCreated(HttpSessionEvent hse) {
                    sessions.put(hse.getSession().getId(), hse.getSession());
                }

                @Override
                public void sessionDestroyed(HttpSessionEvent hse) {
                    sessions.remove(hse.getSession().getId());
                }
            };
        }
    }

