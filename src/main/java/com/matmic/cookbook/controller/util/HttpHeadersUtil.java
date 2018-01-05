package com.matmic.cookbook.controller.util;

import org.springframework.http.HttpHeaders;

public final class HttpHeadersUtil {

    private HttpHeadersUtil(){
    }

    public static HttpHeaders createAlertHeader(String message, String param){
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-cookbookApp-alert", message);
        headers.add("X-cookbookApp-param", param);
        return headers;
    }

    public static HttpHeaders createdEntityAlert(String entityName, String param){
        return createAlertHeader("New " + entityName + " is created with id " + param, param);
    }

    public static HttpHeaders updateEntityAlert(String entityName, String param){
        return createAlertHeader("A " + entityName + " is updated with id " + param, param);
    }

    public static HttpHeaders deleteEntityAlert(String entityName, String param){
        return createAlertHeader("A " + entityName + " is deleted with id " + param, param);
    }

    public static HttpHeaders createEntityFailureAlert(String entityName, String errorMsg){
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-cookbookApp-error", errorMsg);
        headers.add("X-cookbookApp-param", entityName);
        return headers;
    }
}
