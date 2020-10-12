package com.peton.useyourwords.dao;

import com.peton.useyourwords.models.FunnyItem;
import com.peton.useyourwords.models.enums.FunnyTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IFunnyItemRepository extends JpaRepository<FunnyItem, Integer> {

    public int countByType(FunnyTypes type);

    public List<FunnyItem> findAllByType(FunnyTypes type);

    public Page<FunnyItem> findByType(FunnyTypes type, Pageable pageable);
}
