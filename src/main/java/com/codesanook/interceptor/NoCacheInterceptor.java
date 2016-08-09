package com.codesanook.interceptor;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NoCacheInterceptor extends HandlerInterceptorAdapter {


    private Log log = LogFactory.getLog(NoCacheInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setHeader("Cache-Control", "max-age=0, no-store");
        return true;
    }


}
