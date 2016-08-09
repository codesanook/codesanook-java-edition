package com.codesanook.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.springframework.stereotype.Service;

@Service(value = "jsonService")
public class JsonService {

    private final ObjectMapper mapper;

    public JsonService() {

        mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JodaModule());
        mapper.configure(com.fasterxml.jackson.databind.SerializationFeature.
                WRITE_DATES_AS_TIMESTAMPS , false);

    }

    public String toJsonString(Object object) throws JsonProcessingException {
        //Object to JSON in String
        String jsonInString = mapper
                .writeValueAsString(object);
        return jsonInString;
    }
}
