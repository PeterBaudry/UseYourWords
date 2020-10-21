package com.peton.useyourwords.controllers.api;

import com.peton.useyourwords.dao.IFunnyItemRepository;
import com.peton.useyourwords.exceptions.BadRequestException;
import com.peton.useyourwords.exceptions.ItemNotFoundException;
import com.peton.useyourwords.models.FunnyItem;
import com.peton.useyourwords.models.enums.FunnyTypes;
import com.peton.useyourwords.services.FunnyItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@RestController
@RequestMapping("/api/funny-items")
public class FunnyItemController {

    //<editor-fold desc="Fields">

    @Autowired
    private FunnyItemService funnyItemService;

    //</editor-fold>

    //<editor-fold desc="Methods">

    private void checkPayload(Map<String, String> payload) {
        if (!payload.containsKey("content") || !payload.containsKey("type")) {
            throw new BadRequestException("The 'content' and 'type' parameters are required");
        }
    }

    //</editor-fold>

    //<editor-fold desc="Mapping methods">

    @GetMapping
    public List<FunnyItem> index(@RequestParam(required = false) FunnyTypes type, @RequestParam(required = false) boolean random) {
        List<FunnyItem> items = type != null ? funnyItemService.findAllByType(type) : funnyItemService.findAll();
        if (random) {
            Collections.shuffle(items);
        }
        return items;
    }

    @PostMapping
    public FunnyItem store(@RequestBody Map<String, String> payload) {
        checkPayload(payload);

        FunnyItem item = new FunnyItem();
        item.setContent(payload.get("content"));
        item.setType(FunnyTypes.valueOf(payload.get("type")));
        return funnyItemService.save(item);
    }

    @GetMapping("/{id}")
    public FunnyItem show(@PathVariable int id) {
        return funnyItemService.findById(id);
    }

    @PostMapping("/{id}")
    public FunnyItem update(@PathVariable int id, @RequestBody Map<String, String> payload) {
        checkPayload(payload);

        FunnyItem item = funnyItemService.findById(id);
        item.setContent(payload.get("content"));
        item.setType(FunnyTypes.valueOf(payload.get("type")));
        return funnyItemService.save(item);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        funnyItemService.deleteById(id);
    }

    //</editor-fold>

}
