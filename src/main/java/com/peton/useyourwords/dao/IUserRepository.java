package com.peton.useyourwords.dao;

import com.peton.useyourwords.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Integer> {

    public Optional<User> findByUsername(String name);
}
