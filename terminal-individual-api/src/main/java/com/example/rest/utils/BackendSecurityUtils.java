package com.example.rest.utils;

import com.example.rest.config.security.TerminalUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

public class BackendSecurityUtils {

    public static Long getUserId(){
        if(SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken){
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            if(token.getPrincipal() instanceof TerminalUserDetails){
                TerminalUserDetails terminalUserDetails = (TerminalUserDetails) token.getPrincipal();
                return terminalUserDetails.getId();
            }
        }
        return null;
    }

    public static TerminalUserDetails getUserDetails(){
        if(SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken){
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            if(token.getPrincipal() instanceof TerminalUserDetails){
                return (TerminalUserDetails) token.getPrincipal();
            }
        }
        return null;
    }
}
