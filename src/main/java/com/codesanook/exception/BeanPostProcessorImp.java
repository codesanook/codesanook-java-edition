package com.codesanook.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.web.servlet.DispatcherServlet;

public class BeanPostProcessorImp implements BeanPostProcessor {

    private Log  log = LogFactory.getLog(BeanPostProcessorImp.class);
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {

       log.info(String.format("get bean name %s",beanName));
        if (bean instanceof DispatcherServlet) {
            ((DispatcherServlet) bean).setThrowExceptionIfNoHandlerFound(true);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }
}
