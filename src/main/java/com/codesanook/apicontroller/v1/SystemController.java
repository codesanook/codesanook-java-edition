package com.codesanook.apicontroller.v1;

import com.codesanook.dto.CorsDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SystemController {


    private Log log = LogFactory.getLog(SystemController.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private Environment env;

    @RequestMapping("/api/beans")
    public List<String> getAllBeanNames() {

        String[] beanNames = applicationContext.getBeanDefinitionNames();
        List<String> beanNameList = new ArrayList<>();
        for (String beanName : beanNames) {
            String name = String.format("name %s, class %s",
                    beanName, applicationContext.getBean(beanName)
                            .getClass().toString());
            beanNameList.add(name);
        }
        return beanNameList;
    }


    @RequestMapping("/env")
    public void env() {
        log.info(String.format("spring.profiles.active = %s", env.getProperty("spring.profiles.active")));
        log.info(String.format("build.number = %s", env.getProperty("build.number")));
    }


    @RequestMapping(value = "/api/cors", method = RequestMethod.GET)
    @ResponseBody
    public CorsDto cors() {
        CorsDto corsDto = new CorsDto();
        corsDto.setStatus("ok");
        return corsDto;
    }


}
