package com.codesanook.exception;

import com.codesanook.viewmodel.UiAlert;
import com.codesanook.viewmodel.UiAlertType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;

@ControllerAdvice(annotations = Controller.class)
public class WebExceptionHandlerAdvice {

    private Log log = LogFactory.getLog(WebExceptionHandlerAdvice.class);

    @ExceptionHandler(value = Exception.class)
    public ModelAndView handleException(Exception exception, HttpServletRequest request,
                                        HttpServletResponse response) {
        logFullError(exception);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("errors/generic-error");
        modelAndView.addObject("exception", exception);
        modelAndView.addObject("url", request.getRequestURL());
        return modelAndView;
    }


    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView handleNoHandlerFoundException(Exception exception,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response) {
        logFullError(exception);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("errors/not-found");
        modelAndView.addObject("exception", exception);
        modelAndView.addObject("url", request.getRequestURL());
        return modelAndView;
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    public RedirectView handleUnauthorizedException(Exception exception,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) {
        logFullError(exception);
        RedirectView rw = new RedirectView("/user/login");
        //rw.setStatusCode(HttpStatus.MOVED_PERMANENTLY); // you might not need this
        FlashMap outputFlashMap = RequestContextUtils.getOutputFlashMap(request);
        if (outputFlashMap != null) {
            UiAlert alert = new UiAlert(UiAlertType.ERROR, exception.getMessage());
            outputFlashMap.put(UiAlert.KEY, alert);
        }
        return rw;
    }


    @ExceptionHandler(value = InvalidActivationException.class)
    public RedirectView handleInvalidActivationException(Exception exception, HttpServletRequest request) {
        logFullError(exception);
        RedirectView rw = new RedirectView("/user/activate");
        FlashMap outputFlashMap = RequestContextUtils.getOutputFlashMap(request);
        if (outputFlashMap != null) {
            UiAlert alert = new UiAlert(UiAlertType.ERROR, exception.getMessage());
            outputFlashMap.put(UiAlert.KEY, alert);
        }
        return rw;
    }

    private void logFullError(Exception exception) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        String stackTrace = sw.toString();
        log.error(exception.getMessage());
        log.error(stackTrace);
    }


}
