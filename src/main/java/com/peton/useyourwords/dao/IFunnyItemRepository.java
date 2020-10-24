package com.peton.useyourwords.dao;

import com.peton.useyourwords.models.FunnyItem;
import com.peton.useyourwords.models.enums.FunnyTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IFunnyItemRepository extends JpaRepository<FunnyItem, Integer> {

    public int countByType(@Param("type") FunnyTypes type);

    public List<FunnyItem> findAllByType(FunnyTypes type);

    public Page<FunnyItem> findByType(FunnyTypes type, Pageable pageable);

    @Query("SELECT f FROM FunnyItem f where f.type = :type order by function('RAND')")
    public List<FunnyItem> findAllByTypeOrderByRandom(@Param("type") FunnyTypes type);
}
