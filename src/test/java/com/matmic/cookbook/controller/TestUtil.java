package com.matmic.cookbook.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class TestUtil {

    public static String asJsonString(final Object object){
        try{
            return new ObjectMapper().writeValueAsString(object);
        }catch(JsonProcessingException ex){
            throw new RuntimeException(ex);
        }
    }

    public static byte[] asJsonBytes(Object object){
        try{
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

            JavaTimeModule module = new JavaTimeModule();
            mapper.registerModule(module);
            return mapper.writeValueAsBytes(object);
        }catch(JsonProcessingException ex){
            throw new RuntimeException(ex);
        }
    }
}
