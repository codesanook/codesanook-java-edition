package com.codesanook.exception;

import com.google.common.base.Throwables;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;

@ControllerAdvice(annotations = RestController.class)
public class ApiExceptionHandlerAdvice {

    private Log log = LogFactory.getLog(WebExceptionHandlerAdvice.class);


    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ApiError exception(Exception exception, HttpServletRequest request,
                              HttpServletResponse response) {
        ResponseStatus responseStatus = AnnotationUtils.findAnnotation(exception.getClass(), ResponseStatus.class);
        if (responseStatus != null) {
            int statusCode = responseStatus.value().value();
            response.setStatus(statusCode);
        } else {

            int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
            response.setStatus(statusCode);
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        String stackTrace = sw.toString();
        log.error(stackTrace);

        ApiError result = new ApiError();
//        Throwable root = Throwables.getRootCause(exception);
//        log.error(root.getStackTrace());
        result.setMessage(exception.getMessage());
        result.setException(exception.getClass().getSimpleName());
        return result;

    }

}
