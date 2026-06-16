package com.swp.autocarwash.loyalty.entity;

import com.swp.autocarwash.customer.entity.Customer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "loyalty_point_balance", schema = "swp_auto_car_wash")
public class LoyaltyPointBalance {
    @Id
    @Column(name = "customer_id", nullable = false)
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ColumnDefault("0")
    @Column(name = "total_points")
    private Integer totalPoints;

    @ColumnDefault("0")
    @Column(name = "accumulated_points")
    private Integer accumulatedPoints;


}