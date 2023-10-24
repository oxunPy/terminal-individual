package com.example.rest.config.security;

import com.example.rest.utils.BackendSecurityUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        TerminalUserDetails terminalUserDetails = BackendSecurityUtils.getUserDetails();
        if(terminalUserDetails != null) {
//            response.sendRedirect("/dashboard/main.do");
//            response.sendRedirect("/page/dealers");
//            response.sendRedirect("/page/products");
//            response.sendRedirect("/page/reports");
//            response.sendRedirect("/page/warehouses");
            response.sendRedirect("/page/companies");
        }
    }
}
