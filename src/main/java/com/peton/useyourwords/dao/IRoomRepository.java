package com.peton.useyourwords.dao;

import com.peton.useyourwords.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoomRepository extends JpaRepository<Room, Integer> {
}
