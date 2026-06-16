package com.swp.autocarwash.loyalty.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tier_benefit", schema = "swp_auto_car_wash")
public class TierBenefit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_tier_id")
    private CustomerTier customerTier;

    @Size(max = 255)
    @NotNull
    @Column(name = "benefit_description", nullable = false)
    private String benefitDescription;


}