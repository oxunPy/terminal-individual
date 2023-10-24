package com.example.rest.utils;


import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class RequestUtil {
    public static String getHead(HttpServletRequest request) {
        return String.format("%s://%s:%s", request.getScheme(), request.getServerName(), request.getServerPort());
    }

    public static String getRequestPath(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        String contextPath = request.getContextPath();
        return requestUri.substring(contextPath.length());
    }

    public static String getRequestURI(HttpServletRequest request, String requestPath) {
        return request.getContextPath() + requestPath;
    }

    public static String getRequestURL(HttpServletRequest request, String requestPath) {
        return getHead(request) + request.getContextPath() + requestPath;
    }

    public static ServletContext getServletContext(HttpServletRequest request) {
        return request.getSession().getServletContext();
    }

    public static Cookie getCookieByName(HttpServletRequest request, String name) {
        if (request.getCookies() == null) {
            return null;
        } else {
            for(int i = 0; i < request.getCookies().length; ++i) {
                if (request.getCookies()[i].getName().equals(name)) {
                    return request.getCookies()[i];
                }
            }

            return null;
        }
    }

    public static void addAttr(HttpServletRequest request, String key, Object value) {
        request.getSession().setAttribute(key, value);
    }

    public static void removeAttr(HttpServletRequest request, String key) {
        request.getSession().removeAttribute(key);
    }
}

