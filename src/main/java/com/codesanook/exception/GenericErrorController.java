package com.codesanook.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/error")
public class GenericErrorController implements ErrorController {

    private Log log = LogFactory.getLog(GenericErrorController.class);
    private final ErrorAttributes errorAttributes;

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @Autowired
    public GenericErrorController(ErrorAttributes errorAttributes) {
        Assert.notNull(errorAttributes, "ErrorAttributes must not be null");
        this.errorAttributes = errorAttributes;
    }


    @RequestMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ApiError errorJson(HttpServletRequest request, HttpServletResponse response) {
        ApiError result = getApiErrorObject(request);
        return result;
    }

    @RequestMapping(value = "")
    public String error(HttpServletRequest request, Model model) {

        ApiError result = getApiErrorObject(request);
        log.debug(String.format("error status code %d", result.getStatusCode()));

        model.addAttribute("error", result);
        switch (result.getStatusCode()) {
            case 404:
                return "errors/not-found";

            case 401:
                return "user/log-in";
            default:
                return "errors/generic-error";
        }
    }

    private String safeGetStringValueFromMap(Map<String, Object> values, String key) {
        if (values.containsKey(key)) {
            return values.get(key).toString();
        }
        return "";
    }

    private Map<String, Object> getErrorAttributes(HttpServletRequest aRequest, boolean includeStackTrace) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(aRequest);
        return errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace);
    }


    private ApiError getApiErrorObject(HttpServletRequest request) {

        Map<String, Object> errors = getErrorAttributes(request, true);

        ApiError result = new ApiError();
        result.setMessage(safeGetStringValueFromMap(errors, "error"));
        String fullClassName = safeGetStringValueFromMap(errors, "exception");
        int lastDot = fullClassName.lastIndexOf('.');
        String className;
        if (lastDot == -1) {
            className = fullClassName;
        } else {
            className = fullClassName.substring(lastDot + 1);
        }
        result.setException(className);

        int statusCode = Integer.parseInt(safeGetStringValueFromMap(errors, "status"));
        result.setStatusCode(statusCode);

        log.debug(String.format("error message %s",result.getMessage()));
        log.debug(String.format("trace %s", safeGetStringValueFromMap(errors, "trace")));
        return result;
    }


}
