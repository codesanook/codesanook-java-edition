package com.codesanook;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAutoConfiguration(
        //exclude={MultipartAutoConfiguration.class}
        //exclude = {ErrorMvcAutoConfiguration.class}
)
@ComponentScan(value = "com.codesanook")
@EnableAspectJAutoProxy
@EnableTransactionManagement
//@EnableRedisHttpSession
public class Application extends SpringBootServletInitializer {

    private static Log log = LogFactory.getLog(Application.class);


    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
        log.debug("codesanook web started");


    }
}
