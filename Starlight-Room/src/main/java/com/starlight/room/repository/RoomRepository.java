package com.starlight.room.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.starlight.room.entity.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByAvailableTrue();

    List<Room> findByRoomTypeIgnoreCase(String roomType);

    List<Room> findByAvailableTrueAndRoomTypeIgnoreCase(String roomType);
}