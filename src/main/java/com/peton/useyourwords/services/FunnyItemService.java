package com.peton.useyourwords.services;

import com.peton.useyourwords.dao.IFunnyItemRepository;
import com.peton.useyourwords.exceptions.ItemNotFoundException;
import com.peton.useyourwords.models.FunnyItem;
import com.peton.useyourwords.models.enums.FunnyTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Service
public class FunnyItemService {

    //<editor-fold desc="Fields">

    @Autowired
    private IFunnyItemRepository funnyItemsRepository;

    //</editor-fold>

    //<editor-fold desc="Methods">

    public List<FunnyItem> findAll() {
        return funnyItemsRepository.findAll();
    }

    public List<FunnyItem> findAllByType(FunnyTypes type) {
        return funnyItemsRepository.findAllByType(type);
    }

    public FunnyItem findById(int id) {
        return funnyItemsRepository.findById(id).orElseThrow(ItemNotFoundException::new);
    }

    public List<FunnyItem> findTenOrderByRandom() {
        List<FunnyItem> items = funnyItemsRepository.findAllOrderByRandom();
        return items.size() > 10 ? items.subList(0, 10) : items;
    }

    @Transactional
    public FunnyItem save(FunnyItem item) {
        return funnyItemsRepository.save(item);
    }

    @Transactional
    public void deleteById(int id) {
        funnyItemsRepository.deleteById(id);
    }

    //</editor-fold>
}
