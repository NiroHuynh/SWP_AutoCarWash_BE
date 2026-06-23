package com.swp.autocarwash.queue.entity;

import com.swp.autocarwash.booking.entity.Booking;
import com.swp.autocarwash.queue.entity.enums.QueueStatus;
import com.swp.autocarwash.station.entity.Station;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Getter
@Setter
@Entity
@Builder
@Table(name = "queue_ticket", schema = "swp_auto_car_wash")
public class QueueTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id")
    private Station station;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @Size(max = 10)
    @NotNull
    @Column(name = "ticket_number", nullable = false, length = 10)
    private String ticketNumber;

    @Size(max = 20)
    @NotNull
    @Column(name = "status", nullable = false, length = 20)
    private QueueStatus status;

    @CreationTimestamp
    @Column(name = "issued_at")
    private Instant issuedAt;

    @NotNull
    @Column(name = "is_booking", nullable = false)
    private Boolean isBooking;

    @Builder.Default
    @ColumnDefault("0")
    @Column(name = "priority_score")
    private Integer priorityScore;


}