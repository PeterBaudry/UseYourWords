package com.peton.useyourwords.services;

import com.peton.useyourwords.dao.IFunnyItemRepository;
import com.peton.useyourwords.dao.IUserRepository;
import com.peton.useyourwords.exceptions.ItemNotFoundException;
import com.peton.useyourwords.models.User;
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
        return userRepository.findAll();
    }


    public User findById(int id) {
        return userRepository.findById(id).orElseThrow(ItemNotFoundException::new);
    }

    @Transactional
    public void deleteById(int id) {
        userRepository.deleteById(id);
    }
    //</editor-fold>
}
