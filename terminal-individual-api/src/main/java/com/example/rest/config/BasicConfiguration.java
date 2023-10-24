package com.example.rest.config;

import com.example.rest.constant.ConstEndpoints;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class BasicConfiguration {

    @Value("${jwt.validity.period}")
    private String JWT_VALIDITY_PERIOD;
    @Value("${jwt.secret.key}")
    private String JWT_SECRET_KEY;

/*
    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder){
        InMemoryUserDetailsManager inMemUserDetailsManager = new InMemoryUserDetailsManager();
        UserDetails admin = User.withUsername("term!na1")
                .password(passwordEncoder.encode("sql123"))
                .roles("Administrator")
                .build();
        if(!inMemUserDetailsManager.userExists("term!na1")) inMemUserDetailsManager.createUser(admin);
        return inMemUserDetailsManager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .antMatchers("/api/v1/**")
            .authenticated()
            .and()
            .httpBasic();
        return http.build();
    }
*/

    @Bean
    public AuthenticationManager authenticationManager() {
        return authentication -> authentication.isAuthenticated() ? authentication : null;
    }

    @Bean
    public BasicAuthenticationFilter basicAuthFilter(){
        return new BasicAuthenticationFilter(authenticationManager());
    }

    @Bean
    public JwtTokenUtil jwtTokenUtil(){
        return new JwtTokenUtil(Long.parseLong(JWT_VALIDITY_PERIOD), JWT_SECRET_KEY);
    }

    @Bean
    public ConstEndpoints constEndpoints(){
        return new ConstEndpoints();
    }
}
