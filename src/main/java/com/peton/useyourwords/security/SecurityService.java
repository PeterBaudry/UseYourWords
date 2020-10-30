package com.peton.useyourwords.security;

import com.peton.useyourwords.dao.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Service
public class SecurityService implements UserDetailsService {

    //<editor-fold desc="Fields">

    @Autowired
    private IUserRepository userRepository;

    //</editor-fold>

    //<editor-fold desc="Methods">

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.isEmpty()) {
            throw new UsernameNotFoundException("username cannot be empty");
        }
        
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("username not found"));
    }

    //</editor-fold>
}
