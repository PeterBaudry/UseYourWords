package com.peton.useyourwords.controllers;

import com.peton.useyourwords.services.FunnyItemService;
import com.peton.useyourwords.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @Autowired
    private RoomService roomService;
    @Autowired
    private FunnyItemService funnyItemService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("rooms", roomService.findAll().subList(0, Math.min(5, roomService.findAll().size())));
        model.addAttribute("funnyItems", funnyItemService.findAll().subList(0, Math.min(5, funnyItemService.findAll().size())));
        return "admin/index";
    }

}