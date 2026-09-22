package com.starlight.booking.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.starlight.booking.client.RoomClient;
import com.starlight.booking.entity.Booking;
import com.starlight.booking.repository.BookingRepository;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomClient roomClient;

    public BookingService(
            BookingRepository bookingRepository,
            RoomClient roomClient) {

        this.bookingRepository = bookingRepository;
        this.roomClient = roomClient;
    }

    public Booking createBooking(Booking booking, Long userId) {

        // Always take user ID from JWT
        booking.setUserId(userId);

        if (booking.getGuests() <= 0) {
            throw new RuntimeException(
                    "Number of guests must be at least 1");
        }

        if (booking.getCheckInDate() == null
                || booking.getCheckOutDate() == null) {

            throw new RuntimeException(
                    "Check-in and check-out dates are required");
        }

        if (!booking.getCheckOutDate()
                .isAfter(booking.getCheckInDate())) {

            throw new RuntimeException(
                    "Check-out date must be after check-in date");
        }

        RoomClient.Room room =
                roomClient.getRoom(booking.getRoomId());

        if (room == null) {
            throw new RuntimeException("Room not found");
        }

        if (!room.isAvailable()) {
            throw new RuntimeException(
                    "Room is not available");
        }

        if (booking.getGuests() > room.getCapacity()) {
            throw new RuntimeException(
                    "Room capacity exceeded");
        }

        List<Booking> overlappingBookings =
                bookingRepository
                        .findByRoomIdAndStatusAndCheckInDateLessThanAndCheckOutDateGreaterThan(
                                booking.getRoomId(),
                                "CONFIRMED",
                                booking.getCheckOutDate(),
                                booking.getCheckInDate()
                        );

        if (!overlappingBookings.isEmpty()) {
            throw new RuntimeException(
                    "Room is already booked for the selected dates");
        }

        booking.setStatus("CONFIRMED");

        return bookingRepository.save(booking);
    }

    public List<Booking> getBookingsByUser(Long userId) {

        return bookingRepository.findByUserId(userId);
    }

    public Booking getBookingById(Long id, Long userId) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Booking not found"));

        checkOwnership(booking, userId);

        return booking;
    }

    public List<Booking> getBookingsByRoom(Long roomId) {

        return bookingRepository.findByRoomId(roomId);
    }

    public Booking cancelBooking(Long id, Long userId) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Booking not found"));

        checkOwnership(booking, userId);

        if ("CANCELLED".equalsIgnoreCase(booking.getStatus())) {
            throw new RuntimeException(
                    "Booking is already cancelled");
        }

        booking.setStatus("CANCELLED");

        return bookingRepository.save(booking);
    }

    public void deleteBooking(Long id, Long userId) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Booking not found"));

        checkOwnership(booking, userId);

        bookingRepository.delete(booking);
    }

    private void checkOwnership(
            Booking booking,
            Long userId) {

        if (!booking.getUserId().equals(userId)) {

            throw new RuntimeException(
                    "You are not authorized to access this booking");
        }
    }
}