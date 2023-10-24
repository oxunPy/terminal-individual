package com.example.rest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.example.rest.controller.web"})
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler(
    "/js/**",
                "/css/**",
                "/flag-icon-css-master/**",
                "/excels/**",
                "/img/**",
                "/media/**",
                "/fonts/**",
                "/plugins/**"
        ).addResourceLocations(
                "classpath:/static/js/",
                "classpath:/static/css/",
                "classpath:/static/flag-icon-css-master/",
                "classpath:/static/excels/",
                "classpath:/static/img/",
                "classpath:/static/media/",
                "classpath:/static/fonts/",
                "classpath:/static/plugins/"
        );
    }

    @Bean
    public LocaleResolver localeResolver(){
        Locale _uz = new Locale("uz");
        CookieLocaleResolver resolver = new CookieLocaleResolver();
        resolver.setDefaultLocale(_uz);
        return resolver;
    }

    @Bean
    public LocaleChangeInterceptor localeInterceptor(){
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeInterceptor());
    }
}
