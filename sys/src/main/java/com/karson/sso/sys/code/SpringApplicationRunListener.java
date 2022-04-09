package com.karson.sso.sys.code;

import com.karson.sso.sys.env.EnvUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

public class SpringApplicationRunListener implements org.springframework.boot.SpringApplicationRunListener, Ordered {
    private final SpringApplication application;

    private final String[] args;

    public SpringApplicationRunListener(SpringApplication application, String[] args) {
        this.application = application;
        this.args = args;
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }

    @Override
    public void starting() {
        org.springframework.boot.SpringApplicationRunListener.super.starting();
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        EnvUtil.setEnvironment(environment);

    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        org.springframework.boot.SpringApplicationRunListener.super.contextPrepared(context);
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        org.springframework.boot.SpringApplicationRunListener.super.contextLoaded(context);
    }

    @Override
    public void started(ConfigurableApplicationContext context) {
        org.springframework.boot.SpringApplicationRunListener.super.started(context);
    }

    @Override
    public void running(ConfigurableApplicationContext context) {
        org.springframework.boot.SpringApplicationRunListener.super.running(context);
    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        org.springframework.boot.SpringApplicationRunListener.super.failed(context, exception);
    }
}
