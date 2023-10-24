package com.example.rest.config;

import com.example.rest.config.security.AccessChecker;
import com.example.rest.config.security.AuthenticationHandler;
import com.example.rest.config.security.TerminalUserDetailsService;
import com.example.rest.utils.Encryption;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final TerminalUserDetailsService terminalUserDetailsService;
    private final JwtTokenFilter jwtTokenFilter;

    private static String[] NOT_FILTER = new String[]{
            "/api/user/login/**",
            "/api/test**",
            "/api-docs", "/api-docs/*", "/swagger-ui", "/swagger-ui/*",
            "/",
            "/img/**",
            "/login",
            "/login.do",
            "/generateQRCode",
            "/company/favicon",
            "/company/logo",
            "/data/i18n",
            "/bot/api/**",
            "/api/v1/product/**"
    };
    public SecurityConfig(TerminalUserDetailsService terminalUserDetailsService, JwtTokenFilter jwtTokenFilter){
        this.terminalUserDetailsService = terminalUserDetailsService;
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(username -> terminalUserDetailsService.getByUserName(username).get()).passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web){
        web
                .ignoring()
                .antMatchers("/css/**", "/js/**", "/media/**", "/fonts/**", "/excel/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //enable CORS and disable CSRF
        http = http.cors().and().csrf().disable();

        // Set session management to stateless
/*        http = http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();*/
        // Set unauthorized requests exception handler
        http = http
                .exceptionHandling()
                .authenticationEntryPoint((request, response, ex) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage()))
                .and();

        // Set permissions on endpoints
        http.authorizeRequests()
                // Our public endpoints
                .antMatchers(NOT_FILTER).permitAll()
                // Our private endpoints
                .antMatchers("api/v1/**").hasAnyRole("Administrator")
                .and()
                .authorizeRequests().anyRequest().access("@accessChecker.check(authentication, request)")
                .and()
                .formLogin()
                    .loginPage("/login.do")
                    .usernameParameter("username").passwordParameter("password").permitAll()
                    .loginProcessingUrl("/login")
                    .successHandler(authenticationHandler())
                    .failureForwardUrl("/login.do?error=true")
                .and()
                .logout()
                    .logoutUrl("/logout.do")
                    .logoutSuccessUrl("/login.do?logout=true")
                .and()
                .exceptionHandling()
                .accessDeniedPage("/login.do?denied=true");

        // Add JWT token filter
        http.addFilterAfter(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }


    @Bean
    public Encryption passwordEncoder(){
        return new Encryption();
    }

    @Bean
    public AuthenticationHandler authenticationHandler(){
        return new AuthenticationHandler();
    }
    @Bean("accessChecker")
    public AccessChecker accessChecker(){
        return new AccessChecker();
    }

    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source =
            new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}
