package com.peton.useyourwords.starters;

import com.peton.useyourwords.dao.IUserRepository;
import com.peton.useyourwords.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Service
@Profile("starter") //DISPO QUE SI ON ACTIVE LE PROFIL "starter"
public class AdminStarter {
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        User admin = userRepository.findByUsername("admin").orElse(new User());
        if (admin.getId() == 0) {
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("password"));
            admin.setAdmin(true);
            this.userRepository.save(admin);
        }
    }
}
