package com.codesanook.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service(value = "system")
public class SystemService {

    private Log log = LogFactory.getLog(SystemService.class);

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Value("${build.number}")
    private long buildNumber;

    @Value("${domain}")
    private String domain;

    @Autowired
    private Environment env;

    @Autowired
    private HttpServletRequest httpServletRequest;

    public long getBuildNumber() {

        if (activeProfile.toLowerCase().equals("test")) {
            Date date = new Date();
            return date.getTime();
        } else {

            return buildNumber;
        }
    }

    public String getDomain() {
        return domain;
    }
}
