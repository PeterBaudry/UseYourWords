package com.peton.useyourwords.services;

import com.peton.useyourwords.dao.IRoomRepository;
import com.peton.useyourwords.exceptions.ItemNotFoundException;
import com.peton.useyourwords.models.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<Room> findAllByOpen(boolean isOpen) {
        return roomRepository.findAllByIsOpen(isOpen);
    }

    public Room findById(int id) {
        return roomRepository.findById(id).orElseThrow(ItemNotFoundException::new);
    }

    public Room save(Room room) {
        return roomRepository.save(room);
    }

    public void deleteById(int id) {
        roomRepository.deleteById(id);
    }

    //</editor-fold>
}
