package com.matmic.cookbook.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{

   private final Logger log = LoggerFactory.getLogger(AuthenticationSuccessHandler.class);

   @Autowired
   private LoginAttemptControlService loginAttemptControlService;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
        throws IOException, ServletException{
        log.debug("User authenticated");
        loginAttemptControlService.loginSucceeded(request.getRemoteAddr());
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
