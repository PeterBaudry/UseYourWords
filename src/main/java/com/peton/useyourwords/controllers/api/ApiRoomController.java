package com.peton.useyourwords.controllers.api;

import com.peton.useyourwords.exceptions.BadRequestException;
import com.peton.useyourwords.models.FunnyItem;
import com.peton.useyourwords.models.Room;
import com.peton.useyourwords.models.enums.FunnyTypes;
import com.peton.useyourwords.services.FunnyItemService;
import com.peton.useyourwords.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@RestController
@RequestMapping("/api/rooms")
public class ApiRoomController {

    //<editor-fold desc="Fields">

    @Autowired
    private RoomService roomService;

    //</editor-fold>

    //<editor-fold desc="Methods">

    private void checkPayload(Map<String, String> payload) {
        if (!payload.containsKey("name") || !payload.containsKey("max_places")) {
            throw new BadRequestException("The 'name' and 'max_places' parameters are required");
        }
    }

    //</editor-fold>

    //<editor-fold desc="Mapping methods">

    @GetMapping
    public List<Room> index(@RequestParam(required = false) boolean open) {
        return open ? roomService.findAllByOpen(true) : roomService.findAll();
    }

    @PostMapping
    public Room store(@RequestBody Map<String, String> payload) {
        checkPayload(payload);

        Room item = new Room();
        item.setName(payload.get("name"));
        item.setMaxPlaces(Integer.parseInt(payload.get("max_places")));
        item.setOpen(true);
        return roomService.save(item);
    }

    @GetMapping("/{id}")
    public Room show(@PathVariable int id) {
        return roomService.findById(id);
    }

    @PostMapping("/{id}")
    public Room update(@PathVariable int id, @RequestBody Map<String, String> payload) {
        checkPayload(payload);

        Room item = roomService.findById(id);
        item.setName(payload.get("name"));
        item.setMaxPlaces(Integer.parseInt(payload.get("max_places")));
        item.setOpen(true);
        return roomService.save(item);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        roomService.deleteById(id);
    }

    //</editor-fold>

}
