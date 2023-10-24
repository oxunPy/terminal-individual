package com.example.rest.config.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class TerminalUserDetails extends User {
    private Long id;
    private String info;
    private String login;
    public TerminalUserDetails(Long id, String info, String login, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
        this.info = info;
        this.login = login;
    }
}
