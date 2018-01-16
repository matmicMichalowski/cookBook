package com.matmic.cookbook.security;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UnauthorizedEntryPointHttp401 implements AuthenticationEntryPoint{

    private final Logger log = LoggerFactory.getLogger(UnauthorizedEntryPointHttp401.class);


    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException, ServletException {

        log.debug("Rejecting access. Pre-authenticated entry point called.");
        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access Denied");
    }
}
