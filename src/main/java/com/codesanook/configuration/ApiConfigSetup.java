package com.codesanook.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class ApiConfigSetup {

    @Autowired
    private Environment env;

    //@Bean(name = "apiConfig")
    @Bean(name="apiConfig")
    public ApiConfig apiConfig() {
        ApiConfig config=  new ApiConfig();
        config.setRootUrl(env.getProperty("api.root.url"));
        return config;
    }
}
