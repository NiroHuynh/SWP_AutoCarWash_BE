package com.swp.autocarwash.booking.repository;

import com.swp.autocarwash.booking.entity.BookingSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingSlotRepository extends JpaRepository<BookingSlot, Integer> {

    List<BookingSlot> findByStationIdAndDateOrderByStartTimeAsc(
            Integer stationId,
            LocalDate date
    );
}
