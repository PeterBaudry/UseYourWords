package com.peton.useyourwords.controllers;

import com.peton.useyourwords.exceptions.ItemNotFoundException;
import com.peton.useyourwords.models.FunnyItem;
import com.peton.useyourwords.models.enums.FunnyTypes;
import com.peton.useyourwords.services.FileService;
import com.peton.useyourwords.services.FunnyItemService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


@Controller
@RequestMapping("/funnyItem")
public class FunnyItemController {

    @Autowired
    private FunnyItemService funnyItemService;
    @Autowired
    private FileService fileService;
    @Autowired
    private Environment env;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("funnyItems", funnyItemService.findAll());
        return "funnyItem/index";
    }
    @GetMapping("/add")
    public String getAdd(Model model) {
        return "funnyItem/add";
    }

    @PostMapping("/add")
    public String postAdd(HttpServletRequest request, @RequestParam FunnyTypes type, @RequestParam(required = false) String content, @RequestParam(required = false) MultipartFile file, Model model) {

        List<FunnyItem> list = funnyItemService.findAll();
        Collections.sort(list, Comparator.comparingInt(FunnyItem::getId));
        int id = list.get(list.size()-1).getId()+1;
        if(content.length() <= 0)
            funnyItemService.save(new FunnyItem(type, StringUtils.cleanPath(id+"."+FilenameUtils.getExtension(file.getOriginalFilename()))));
        else
            funnyItemService.save(new FunnyItem(type, content));

        fileService.uploadFile(file, id);
        return "redirect:/funnyItem";
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        try {
            fileService.deleteFile(env.getProperty("app.upload.dir")+funnyItemService.findById(id).getContent());
            funnyItemService.deleteById(id);
            return "redirect:/funnyItem";
        } catch (ItemNotFoundException | IOException e) {
            return "errors/404";
        }
    }
}