package com.peton.useyourwords.services;

import com.peton.useyourwords.dao.IRoomRepository;
import com.peton.useyourwords.exceptions.ItemNotFoundException;
import com.peton.useyourwords.models.FunnyItem;
import com.peton.useyourwords.models.Room;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Service
public class RoomService {

    //<editor-fold desc="Fields">

    @Autowired
    private IRoomRepository roomRepository;

    //</editor-fold>

    //<editor-fold desc="Methods">

    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    public List<Room> findAllByIsOpen(boolean isOpen) {
        return roomRepository.findAllByIsOpen(isOpen);
    }

    public Room findById(int id) {
        Room room = roomRepository.findById(id).orElseThrow(ItemNotFoundException::new);
        Hibernate.initialize(room.getUsers());
        Hibernate.initialize(room.getFunnyItems());
        return room;
    }

    @Transactional
    public Room save(Room room) {
        roomRepository.save(room);
        Hibernate.initialize(room.getUsers());
        Hibernate.initialize(room.getFunnyItems());
        return room;
    }

    @Transactional
    public void deleteById(int id) {
        Room item = findById(id);
        item.setFunnyItems(null);
        roomRepository.save(item);
        roomRepository.deleteById(id);
    }

    //</editor-fold>
}
