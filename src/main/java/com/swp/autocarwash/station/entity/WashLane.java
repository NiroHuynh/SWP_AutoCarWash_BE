package com.swp.autocarwash.station.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "wash_lane", schema = "swp_auto_car_wash")
public class WashLane {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    @Size(max = 50)
    @NotNull
    @Column(name = "lane_name", nullable = false, length = 50)
    private String laneName;

    @Size(max = 20)
    @NotNull
    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @ColumnDefault("3")
    @Column(name = "booking_walkin_ratio")
    private Integer bookingWalkinRatio;

    @ColumnDefault("0")
    @Column(name = "is_deleted")
    private Boolean isDeleted;


}