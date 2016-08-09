package com.codesanook.configuration;

import com.codesanook.filter.CorsFilter;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfiguration {

    //minimum number come first
    private final int STATIC_FILTER_ORDER = 10;
    private final int CORS_FILTER_ORDER = 20;
    private final int LOGGED_IN_USER_ORDER = 30;

    @Bean
    public FilterRegistrationBean corsFilter() {

        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new CorsFilter());
        registration.addUrlPatterns("/api/*");
        registration.setOrder(CORS_FILTER_ORDER);

        return registration;
    }

//    @Bean
//    public FilterRegistrationBean loggedInUserFilter() {
//

    //registration.addUrlPatterns("/url/*");
    //registration.addInitParameter("paramName", "paramValue");
    //registration.setName("someFilter");
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//        registration.setFilter(new LoggedInUserFilter());
//        registration.addUrlPatterns("/*");
//        registration.setOrder(LOGGED_IN_USER_ORDER);
//        return registration;
//    }

}
