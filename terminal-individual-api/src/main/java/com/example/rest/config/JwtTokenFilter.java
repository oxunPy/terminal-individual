package com.example.rest.config;

import com.example.rest.config.security.TerminalUserDetails;
import com.example.rest.config.security.TerminalUserDetailsService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    private final JwtTokenUtil jwtTokenUtil;
    private final TerminalUserDetailsService terminalUserDetailsService;

    public JwtTokenFilter(JwtTokenUtil jwtTokenUtil, TerminalUserDetailsService terminalUserDetailsService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.terminalUserDetailsService = terminalUserDetailsService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String header =  request.getHeader(HttpHeaders.AUTHORIZATION);

        if(header == null || header.isEmpty() || !header.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        /*Get jwt token and validate*/
        String token = header.split(" ")[1].trim();
        if(!jwtTokenUtil.validate(token)){
            filterChain.doFilter(request, response);
            return;
        }
        // Get user identity and set it on the spring security context
        TerminalUserDetails terminalUserDetails = terminalUserDetailsService.getByUserName(jwtTokenUtil.getSubjectFromToken(token)).orElse(null);

        UsernamePasswordAuthenticationToken
                authentication = new UsernamePasswordAuthenticationToken(
                terminalUserDetails, null, terminalUserDetails == null ?  new ArrayList<>(): terminalUserDetails.getAuthorities());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
