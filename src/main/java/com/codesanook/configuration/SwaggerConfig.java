package com.codesanook.configuration;

import com.mangofactory.swagger.plugin.EnableSwagger;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableSwagger
public class SwaggerConfig {

//    private SpringSwaggerConfig springSwaggerConfig;
//
//    @Autowired
//    public void setSpringSwaggerConfig(SpringSwaggerConfig springSwaggerConfig) {
//        this.springSwaggerConfig = springSwaggerConfig;
//    }
//
//    @Bean
//    public SwaggerSpringMvcPlugin customImplementation() {
//        return new SwaggerSpringMvcPlugin(this.springSwaggerConfig)
//                .useDefaultResponseMessages(false)
//                .swaggerGroup("customers")
//                .includePatterns("/customers.*");
//
//    }
//
//    @Bean
//    public SwaggerSpringMvcPlugin customImplementation2() {
//        return new SwaggerSpringMvcPlugin(this.springSwaggerConfig)
//                .swaggerGroup("users")
//                .includePatterns(".*/users.*");
//    }

}