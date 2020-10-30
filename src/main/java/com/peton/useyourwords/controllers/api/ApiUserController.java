package com.peton.useyourwords.controllers.api;

import com.peton.useyourwords.exceptions.BadRequestException;
import com.peton.useyourwords.exceptions.InvalidCredentialsException;
import com.peton.useyourwords.exceptions.UserInDifferentRoomException;
import com.peton.useyourwords.models.Room;
import com.peton.useyourwords.models.User;
import com.peton.useyourwords.services.RoomService;
import com.peton.useyourwords.services.UserService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

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

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> payload) {
        checkPayload(payload);

        User user = userService.findByUsername(payload.get("username"));

        if (!passwordEncoder.matches(payload.get("password"), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String userCredentials = payload.get("username") + ":" + payload.get("password");

        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("token", new String(Base64.encodeBase64(userCredentials.getBytes())));

        return map;
    }

    @GetMapping
    public List<User> index() {
        return userService.findAll();
    }

    @PostMapping("/register")
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

        Map<String, Object> map = new HashMap<>();

        item.incrementPoints();
        userService.save(item);

        Room room = roomService.findById(roomId);

        room.incrementCurrentUserActionsCount();
        if (room.getCurrentUserActionsCount() == room.getUsers().size()) {
            room.setCurrentUserActionsCount(0);
            room.incrementCurrentRound();

            if (room.getCurrentRound() < room.getFunnyItems().size()) {
                room.setCurrentState("play");
            } else {
                room.getUsers().sort(Comparator.comparing(User::getPoints));
                int winnerPoints = room.getUsers().get(room.getUsers().size() - 1).getPoints();
                int[] winnerIds = room.getUsers().stream().filter(u -> u.getPoints() == winnerPoints).mapToInt(u -> u.getPoints()).toArray();
                room.setCurrentState("end");
                map.put("winner_ids", winnerIds);
            }
        }
        roomService.save(room);

        map.put("room", roomService.findById(roomId));
        map.put("message", activeUser.getUsername() + " a voté !");

        messagingTemplate.convertAndSend("/rooms/" + roomId, map);

        return item;
    }

    //</editor-fold>

}
