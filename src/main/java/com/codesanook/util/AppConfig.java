package com.codesanook.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConfig {

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Value("${domain}")
    private String domain;

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    public String getActiveProfile() {
        return this.activeProfile;
    }

    public String getDomain() {
        return this.domain;
    }

    public String getDatasourceUrl() {


        return datasourceUrl;
    }



}
