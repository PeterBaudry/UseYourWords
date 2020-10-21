package com.peton.useyourwords.dao;

import com.peton.useyourwords.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IRoomRepository extends JpaRepository<Room, Integer> {
    public List<Room> findAllByIsOpen(boolean isOpen);
}
