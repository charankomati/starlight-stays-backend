package com.starlight.booking.repository;

import com.starlight.booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserId(Long userId);

    List<Booking> findByRoomId(Long roomId);

    List<Booking> findByStatus(String status);

    List<Booking> findByRoomIdAndStatusAndCheckInDateLessThanAndCheckOutDateGreaterThan(
            Long roomId,
            String status,
            LocalDate checkOutDate,
            LocalDate checkInDate
    );
}