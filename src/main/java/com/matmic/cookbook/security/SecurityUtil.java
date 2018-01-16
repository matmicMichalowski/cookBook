package com.matmic.cookbook.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public final class SecurityUtil {

    private SecurityUtil(){
    }

    public static String getCurrentUser(){
        SecurityContext sctx = SecurityContextHolder.getContext();
        Authentication authentication = sctx.getAuthentication();
        String currentUsername = null;
        if (authentication != null){
            if (authentication.getPrincipal() instanceof UserDetails){
                UserDetails user = (UserDetails) authentication.getPrincipal();
                currentUsername = user.getUsername();
            }else if(authentication.getPrincipal() instanceof String){
                currentUsername = (String) authentication.getPrincipal();
            }
        }
        return currentUsername;
    }

    public static boolean checkIsAuthenticated(){
        SecurityContext sctx = SecurityContextHolder.getContext();
        Authentication authentication = sctx.getAuthentication();
        if (authentication != null){
            return authentication.getAuthorities().stream().noneMatch(
                    auth -> auth.getAuthority().equals(AuthoritiesConstants.ANONYMOUS));

        }
        return false;
    }

    public static boolean isCurrentUserInRole(String authority){
        SecurityContext sctx = SecurityContextHolder.getContext();
        Authentication authentication = sctx.getAuthentication();
        if (authentication != null){
            return authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(authority));
        }
        return false;
    }
}
