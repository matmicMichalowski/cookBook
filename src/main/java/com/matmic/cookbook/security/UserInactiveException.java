package com.matmic.cookbook.security;

import org.springframework.security.core.AuthenticationException;

public class UserInactiveException extends AuthenticationException{

    public UserInactiveException(String exceptionMsg){
        super(exceptionMsg);
    }

}
