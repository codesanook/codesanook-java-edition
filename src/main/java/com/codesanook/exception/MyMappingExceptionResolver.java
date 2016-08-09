package com.codesanook.exception;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyMappingExceptionResolver extends SimpleMappingExceptionResolver {
    public MyMappingExceptionResolver() {
        // Enable logging by providing the name of the logger to use
        setWarnLogCategory(MyMappingExceptionResolver.class.getName());
    }

    @Override
    public String buildLogMessage(Exception e, HttpServletRequest req) {
        return "MVC exception: " + e.getLocalizedMessage();
    }

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request,
                                              HttpServletResponse response,
                                              Object handler, Exception exception) {
        // Call super method to get the ModelAndView
        ModelAndView mav = super.doResolveException(request, response, handler, exception);

        // Make the full URL available to the view - note ModelAndView uses addObject()
        // but Model uses addAttribute(). They work the same.
        mav.addObject("url", request.getRequestURL());
        mav.setViewName("not-found");
        return mav;
    }
}
