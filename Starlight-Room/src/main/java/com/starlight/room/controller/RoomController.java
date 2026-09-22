package com.starlight.room.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.starlight.room.entity.Room;
import com.starlight.room.service.RoomService;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    public ResponseEntity<Room> addRoom(@RequestBody Room room) {
        return ResponseEntity.ok(roomService.addRoom(room));
    }

    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @GetMapping("/available")
    public ResponseEntity<List<Room>> getAvailableRooms() {
        return ResponseEntity.ok(roomService.getAvailableRooms());
    }

    @GetMapping("/type/{roomType}")
    public ResponseEntity<List<Room>> getRoomsByType(
            @PathVariable String roomType) {
        return ResponseEntity.ok(
                roomService.getRoomsByType(roomType)
        );
    }

    @GetMapping("/available/type/{roomType}")
    public ResponseEntity<List<Room>> getAvailableRoomsByType(
            @PathVariable String roomType) {
        return ResponseEntity.ok(
                roomService.getAvailableRoomsByType(roomType)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                roomService.getRoomById(id)
        );
    }

    @PutMapping("/{id}/availability")
    public ResponseEntity<Room> updateAvailability(
            @PathVariable Long id,
            @RequestParam boolean available) {

        return ResponseEntity.ok(
                roomService.updateAvailability(id, available)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRoom(
            @PathVariable Long id) {

        roomService.deleteRoom(id);

        return ResponseEntity.ok("Room deleted successfully");
    }
}