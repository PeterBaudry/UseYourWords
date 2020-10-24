package com.peton.useyourwords.controllers.api;

import com.peton.useyourwords.exceptions.BadRequestException;
import com.peton.useyourwords.exceptions.UserInDifferentRoomException;
import com.peton.useyourwords.models.Room;
import com.peton.useyourwords.models.User;
import com.peton.useyourwords.services.RoomService;
import com.peton.useyourwords.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@RestController
@RequestMapping("/api/rooms")
public class ApiRoomController {

    //<editor-fold desc="Fields">

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private RoomService roomService;

    @Autowired
    private UserService userService;

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
        return open ? roomService.findAllByIsOpen(true) : roomService.findAll();
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
        return roomService.save(item);
    }

    @GetMapping("/{id}/join")
    public Room join(@PathVariable int id, Principal principal) {
        User activeUser = userService.findByUsername(principal.getName());
        Room item = roomService.findById(id);

        activeUser.setRoom(item);
        userService.save(activeUser);
        messagingTemplate.convertAndSend("/rooms/" + id, activeUser.getUsername() + " a rejoint la room !");
        /*messagingTemplate.convertAndSend("/live/rooms/" + id, new Object() {
            Room room = roomService.findById(id);
            String message = activeUser.getUsername() + " a rejoint la room !";
        });*/

        return item;
    }

    @GetMapping("/{id}/leave")
    public Room leave(@PathVariable int id, Principal principal) {
        User activeUser = userService.findByUsername(principal.getName());
        Room item = roomService.findById(id);

        activeUser.setRoom(null);
        userService.save(activeUser);
        messagingTemplate.convertAndSend("/rooms/" + activeUser.getUsername() + " a quitté la room !");
        /*messagingTemplate.convertAndSend("/live/rooms/" + id, new Object() {
            Room room = roomService.findById(id);
            String message = activeUser.getUsername() + " a quitté la room !";
        });*/

        return item;
    }

    @GetMapping("/{id}/close")
    public Room close(@PathVariable int id) {
        Room item = roomService.findById(id);
        item.setOpen(false);

        messagingTemplate.convertAndSend("/rooms/" + id, new Object() {
            Room room = roomService.findById(id);
            String message = item.getName() + " est verrouillé !";
        });

        return roomService.save(item);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        roomService.deleteById(id);
    }

    //</editor-fold>

}
