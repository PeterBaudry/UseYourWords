package com.peton.useyourwords.services;

import com.peton.useyourwords.dao.IFunnyItemRepository;
import com.peton.useyourwords.dao.IUserRepository;
import com.peton.useyourwords.exceptions.ItemNotFoundException;
import com.peton.useyourwords.models.Room;
import com.peton.useyourwords.models.User;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Service
public class UserService {

    //<editor-fold desc="Fields">

    @Autowired
    private IUserRepository userRepository;

    //</editor-fold>

    //<editor-fold desc="Methods">

    public List<User> findAll() {
        List<User> users = userRepository.findAll();
        users.forEach(user -> Hibernate.initialize(user.getRoom()));
        return users;
    }


    public User findById(int id) {
        User user = userRepository.findById(id).orElseThrow(ItemNotFoundException::new);
        Hibernate.initialize(user.getRoom());
        return user;
    }

    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(ItemNotFoundException::new);
        Hibernate.initialize(user.getRoom());
        return user;
    }

    @Transactional
    public User save(User user) {
        userRepository.save(user);
        Hibernate.initialize(user.getRoom());
        return user;
    }

    @Transactional
    public void deleteById(int id) {
        userRepository.deleteById(id);
    }

    //</editor-fold>
}
