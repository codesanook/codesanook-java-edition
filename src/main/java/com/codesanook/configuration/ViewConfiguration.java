package com.codesanook.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class ViewConfiguration {

    @Autowired
    private Environment environment;

    @Bean(name = "env")
    public Environment getEnvironment() {
        Environment env =  environment;
        return env;
    }






}
