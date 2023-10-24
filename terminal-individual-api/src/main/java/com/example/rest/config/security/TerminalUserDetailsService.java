package com.example.rest.config.security;

import com.example.rest.dto.dtoProjection.UserDtoProjection;
import com.example.rest.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class TerminalUserDetailsService {
    private final UserRepository userRepository;

    public TerminalUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public Optional<TerminalUserDetails> getByUserName(String username){
        Optional<UserDtoProjection> userOpt = userRepository.findByLogin(username);
        //            throw new UsernameNotFoundException(String.format("Username %s, not found", username));
        return userOpt.map(userDtoProjection ->
                                            new TerminalUserDetails(userDtoProjection.getId(),
                                                                    userDtoProjection.getInfo(),
                                                                    userDtoProjection.getLogin(),
                                                                    userDtoProjection.getUser_name(),
                                                                    userDtoProjection.getPass(),
                                                                    Collections.singletonList(new SimpleGrantedAuthority(userDtoProjection.getRole()))));

    }
}
