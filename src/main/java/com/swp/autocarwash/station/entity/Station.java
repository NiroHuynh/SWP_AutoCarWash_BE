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
@Table(name = "station", schema = "swp_auto_car_wash")
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "station_name", nullable = false, length = 100)
    private String stationName;

    @Size(max = 255)
    @NotNull
    @Column(name = "address", nullable = false)
    private String address;

    @ColumnDefault("1")
    @Column(name = "is_operating")
    private Boolean isOperating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commune_id")
    private Commune commune;

    @ColumnDefault("0")
    @Column(name = "max_wash_capacity")
    private Integer maxWashCapacity;

    @ColumnDefault("0")
    @Column(name = "is_deleted")
    private Boolean isDeleted;


}