package com.peton.useyourwords.controllers;

import com.peton.useyourwords.exceptions.ItemNotFoundException;

import com.peton.useyourwords.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("users", userService.findAll());
        return "user/index";
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        try {
            userService.deleteById(id);
            return "redirect:/users";
        } catch (ItemNotFoundException e) {
            return "errors/404";
        }
    }
}