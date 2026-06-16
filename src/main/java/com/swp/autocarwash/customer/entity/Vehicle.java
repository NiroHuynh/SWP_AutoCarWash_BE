package com.swp.autocarwash.customer.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "vehicle", schema = "swp_auto_car_wash")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Size(max = 20)
    @NotNull
    @Column(name = "license_plate", nullable = false, length = 20)
    private String licensePlate;

    @Size(max = 50)
    @NotNull
    @Column(name = "brand_name", nullable = false, length = 50)
    private String brandName;

    @Size(max = 30)
    @Column(name = "color", length = 30)
    private String color;

    @Column(name = "violation_count")
    private Integer violationCount;

    @Column(name = "restricted_until")
    private Instant restrictedUntil;

    @ColumnDefault("0")
    @Column(name = "is_deleted")
    private Boolean isDeleted;


}