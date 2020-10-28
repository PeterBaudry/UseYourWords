package com.peton.useyourwords.controllers.api;

import com.peton.useyourwords.exceptions.BadRequestException;
import com.peton.useyourwords.exceptions.UserInDifferentRoomException;
import com.peton.useyourwords.models.Room;
import com.peton.useyourwords.models.User;
import com.peton.useyourwords.services.FunnyItemService;
import com.peton.useyourwords.services.RoomService;
import com.peton.useyourwords.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
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

    @Autowired
    private FunnyItemService funnyItemService;

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
        item.setCurrentRound(0);
        item.setCurrentState("play");
        item.setCurrentUserActionsCount(0);
        item.setMaxPlaces(Integer.parseInt(payload.get("max_places")));
        item.setOpen(true);
        item.setFunnyItems(funnyItemService.findTenOrderByRandom());

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

        Map<String, Object> map = new HashMap<>();
        map.put("room", roomService.findById(id));
        map.put("message", activeUser.getUsername() + " a rejoint la room !");

        messagingTemplate.convertAndSend("/rooms/" + id, map);

        return item;
    }

    @GetMapping("/{id}/leave")
    public Room leave(@PathVariable int id, Principal principal) {
        User activeUser = userService.findByUsername(principal.getName());
        Room item = roomService.findById(id);

        activeUser.setRoom(null);
        activeUser.setPoints(0);
        userService.save(activeUser);

        item.removeUser(activeUser);
        if (item.getUsers().size() > 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("room", roomService.findById(id));
            map.put("message", activeUser.getUsername() + " a quitté le salon !");

            messagingTemplate.convertAndSend("/rooms/" + id, map);

            return item;
        } else {
            roomService.deleteById(id);
            return null;
        }
    }

    @PostMapping("/{id}/message")
    public Room message(@PathVariable int id, Principal principal, @RequestBody Map<String, String> payload) {
        User activeUser = userService.findByUsername(principal.getName());
        Room item = roomService.findById(id);

        if (activeUser.getRoom().getId() != id) {
            throw new UserInDifferentRoomException();
        }

        if (!payload.containsKey("sentence")) {
            throw new BadRequestException("The 'sentence' field is required.");
        }

        item.incrementCurrentUserActionsCount();
        if (item.getCurrentUserActionsCount() == item.getUsers().size()) {
            item.setCurrentUserActionsCount(0);
            item.setCurrentState("vote");
        }
        roomService.save(item);

        Map<String, Object> map = new HashMap<>();
        map.put("room", roomService.findById(id));
        map.put("message", activeUser.getUsername() + " a submit sa phrase !");
        map.put("sentence", payload.get("sentence"));
        map.put("userId", activeUser.getId());

        messagingTemplate.convertAndSend("/rooms/" + id, map);

        return item;
    }

    @GetMapping("/{id}/close")
    public Room close(@PathVariable int id) {
        Room item = roomService.findById(id);
        item.setOpen(false);

        Map<String, Object> map = new HashMap<>();
        map.put("room", roomService.findById(id));
        map.put("message", item.getName() + " est verrouillé !");

        return roomService.save(item);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        roomService.deleteById(id);
    }

    //</editor-fold>

}
