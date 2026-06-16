package com.swp.autocarwash.promotion.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "promotion_target", schema = "swp_auto_car_wash")
public class PromotionTarget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "target_name", nullable = false, length = 50)
    private String targetName;

    @Size(max = 50)
    @NotNull
    @Column(name = "target_code", nullable = false, length = 50)
    private String targetCode;

    @Size(max = 255)
    @Column(name = "description")
    private String description;


}