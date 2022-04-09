package com.karson.sso.sys.env;

import org.springframework.core.env.ConfigurableEnvironment;

public class EnvUtil {
    private static ConfigurableEnvironment environment;

    public static ConfigurableEnvironment getEnvironment() {
        return environment;
    }

    public static void setEnvironment(ConfigurableEnvironment environment) {
        EnvUtil.environment = environment;
    }

}
