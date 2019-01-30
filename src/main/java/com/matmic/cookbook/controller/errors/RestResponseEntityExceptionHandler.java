package com.matmic.cookbook.controller.errors;

import org.springframework.boot.context.properties.source.InvalidConfigurationPropertyValueException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler{

//    TODO wyszukać poprawny typ Exception, aktualnie podstawiony został mock excption, pożądany typ to odpowiednik usuniętego ResourceNotFoundException

    @ExceptionHandler({InvalidConfigurationPropertyValueException.class})
    public ResponseEntity<String> handleResourceNotFound(InvalidConfigurationPropertyValueException ex, WebRequest request){
        return new ResponseEntity<>("Resource Not Found", new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

}
