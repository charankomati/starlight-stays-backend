package com.starlight.room.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.starlight.room.entity.Room;
import com.starlight.room.repository.RoomRepository;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room addRoom(Room room) {
        return roomRepository.save(room);
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public List<Room> getAvailableRooms() {
        return roomRepository.findByAvailableTrue();
    }

    public List<Room> getRoomsByType(String roomType) {
        return roomRepository.findByRoomTypeIgnoreCase(roomType);
    }

    public List<Room> getAvailableRoomsByType(String roomType) {
        return roomRepository.findByAvailableTrueAndRoomTypeIgnoreCase(roomType);
    }

    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
    }

    public Room updateAvailability(Long id, boolean available) {
        Room room = getRoomById(id);
        room.setAvailable(available);
        return roomRepository.save(room);
    }

    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }
}