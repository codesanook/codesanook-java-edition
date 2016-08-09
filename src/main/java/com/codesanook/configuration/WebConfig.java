package com.codesanook.configuration;

import com.codesanook.interceptor.LoggedInUserRequestHandler;
import com.codesanook.interceptor.NoCacheInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    //from
    //http://stackoverflow.com/questions/21404102/how-to-inject-session-scope-bean-in-interceptor-using-java-config-with-spring
    @Bean
    public LoggedInUserRequestHandler loggedInUserRequestHandler() {
        return new LoggedInUserRequestHandler();
    }

    @Bean
    public NoCacheInterceptor noCacheInterceptor() {
        return new NoCacheInterceptor();
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggedInUserRequestHandler())
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login");

        registry.addInterceptor(noCacheInterceptor());
        registry.addInterceptor(new NoCacheInterceptor())
                .addPathPatterns("/**");

        registry.addInterceptor(localeChangeInterceptor());
    }

    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver resolver = new CookieLocaleResolver();
        resolver.setDefaultLocale(new Locale("th"));
        resolver.setCookiePath("/");
        resolver.setCookieName("cs-user-local");
        int ageInSeconds = 30 * 24 * 60 * 60;
        resolver.setCookieMaxAge(ageInSeconds);
        return resolver;
    }


//    @Bean
//    public BeanPostProcessor BeanPostProcessor  () {
//        return  new BeanPostProcessorImp();
//    }


//    @Bean(name = "simpleMappingExceptionResolver")
//    public SimpleMappingExceptionResolver createSimpleMappingExceptionResolver() {
//        SimpleMappingExceptionResolver r =
//                new SimpleMappingExceptionResolver();
//
//        Properties mappings = new Properties();
//        mappings.setProperty("DatabaseException", "databaseError");
//        mappings.setProperty("InvalidCreditCardException", "creditCardError");
//
//        r.setExceptionMappings(mappings);  // None by default
//        //r.setDefaultErrorView("error");    // No default
//        r.setExceptionAttribute("ex");     // Default is "exception"
//        r.setWarnLogCategory("example.MvcLogger");     // No default
//        return r;
//    }


//    @Bean
//    public EmbeddedServletContainerCustomizer containerCustomizer(){
//        return new EmbeddedServletContainerCustomizerImp();
//    }
}
