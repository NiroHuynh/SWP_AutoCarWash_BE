package com.swp.autocarwash.booking.repository;

import com.swp.autocarwash.booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
}
