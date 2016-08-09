package com.codesanook.configuration;

import com.codesanook.markdown.CustomPegDownProcessor;
import com.codesanook.repository.MarkDownRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class PegDownConfiguration {

    @Autowired
    private MarkDownRepository markDownRepository;

    @Autowired
    private Environment env;

    @Bean
    public CustomPegDownProcessor pegDownProcessor() {
        CustomPegDownProcessor pegDownProcessor =new CustomPegDownProcessor(env,markDownRepository);
        return pegDownProcessor;
    }
}
