package com.example.rest.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import javax.servlet.http.HttpServletRequest;

public class AccessChecker {
    public boolean check(Authentication authentication, HttpServletRequest request){
        return check(authentication, request.getRequestURI());
    }

    public boolean check(Authentication authentication, String uri){
        if(authentication.getPrincipal() != null && authentication.getPrincipal() instanceof TerminalUserDetails){
            TerminalUserDetails terminalUserDetails = (TerminalUserDetails) authentication.getPrincipal();
            return terminalUserDetails.getAuthorities().contains(new SimpleGrantedAuthority("Administrator"));
        }
        return false;
    }
}