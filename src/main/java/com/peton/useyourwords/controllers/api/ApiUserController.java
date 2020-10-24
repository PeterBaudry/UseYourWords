package com.peton.useyourwords.controllers.api;

import com.peton.useyourwords.exceptions.BadRequestException;
import com.peton.useyourwords.exceptions.UserInDifferentRoomException;
import com.peton.useyourwords.models.Room;
import com.peton.useyourwords.models.User;
import com.peton.useyourwords.services.RoomService;
import com.peton.useyourwords.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@RestController
@RequestMapping("/api/users")
public class ApiUserController {

    //<editor-fold desc="Fields">

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //</editor-fold>

    //<editor-fold desc="Methods">

    private void checkPayload(Map<String, String> payload) {
        if (!payload.containsKey("username") || !payload.containsKey("password")) {
            throw new BadRequestException("The 'username' and 'password' parameters are required");
        }
    }

    //</editor-fold>

    //<editor-fold desc="Mapping methods">

    @GetMapping
    public List<User> index() {
        return userService.findAll();
    }

    @PostMapping
    public User store(@RequestBody Map<String, String> payload) {
        checkPayload(payload);

        User item = new User();
        item.setUsername(payload.get("username"));
        item.setPassword(passwordEncoder.encode(payload.get("password")));
        item.setAdmin(false);
        item.setWins(0);
        item.setLosses(0);
        item.setPoints(0);
        return userService.save(item);
    }

    @GetMapping("/{id}")
    public User show(@PathVariable int id) {
        return userService.findById(id);
    }

    @PostMapping("/{id}")
    public User update(@PathVariable int id, @RequestBody Map<String, String> payload) {
        checkPayload(payload);

        User item = userService.findById(id);
        item.setUsername(payload.get("username"));
        item.setPassword(passwordEncoder.encode(payload.get("password")));
        return userService.save(item);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        userService.deleteById(id);
    }

    @GetMapping("/{id}/vote")
    public User vote(@PathVariable int id, Principal principal) {
        User activeUser = userService.findByUsername(principal.getName());
        int roomId = activeUser.hasRoom() ? activeUser.getRoom().getId() : 0;
        User item = userService.findById(id);

        if (roomId != 0 && item.hasRoom()) {
            if (roomId != item.getRoom().getId()) {
                throw new UserInDifferentRoomException();
            }
        } else {
            throw new UserInDifferentRoomException();
        }

        item.incrementPoints();
        userService.save(item);

        messagingTemplate.convertAndSend("/live/rooms/" + roomId, new Object() {
            Room room = roomService.findById(roomId);
            String message = activeUser.getUsername() + " a voté !";
        });

        return item;
    }

    //</editor-fold>

}
