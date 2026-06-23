package com.swp.autocarwash.booking.entity;

import com.swp.autocarwash.station.entity.Station;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "booking_slot", schema = "swp_auto_car_wash")
public class BookingSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id")
    private Station station;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @NotNull
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @NotNull
    @Column(name = "max_capacity", nullable = false)
    private Integer maxCapacity;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ColumnDefault("0")
    @Column(name = "booked_count")
    private Integer bookedCount;

    @Size(max = 20)
    @NotNull
    @Column(name = "status", nullable = false, length = 20)
    private String status;


    // derived field (KHÔNG lưu DB)
    public int getAvailableCapacity() {
        return maxCapacity - bookedCount;
    }
}