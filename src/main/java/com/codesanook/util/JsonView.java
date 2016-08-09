package com.codesanook.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonView {

    public static ModelAndView Render(Object model, HttpServletResponse response){

        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(model);
            writeJson(jsonString, response);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    private static void writeJson(String jsonString, HttpServletResponse response) throws IOException {

        AbstractHttpMessageConverter<String> stringHttpMessageConverter = new StringHttpMessageConverter();
        MediaType jsonMimeType = MediaType.APPLICATION_JSON;
            stringHttpMessageConverter.write(jsonString, jsonMimeType, new ServletServerHttpResponse(response));
    }
}

