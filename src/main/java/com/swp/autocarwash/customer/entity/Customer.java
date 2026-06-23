package com.swp.autocarwash.customer.entity;

import com.swp.autocarwash.auth.entity.User;
import com.swp.autocarwash.loyalty.entity.CustomerTier;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "customer", schema = "swp_auto_car_wash")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Size(max = 100)
    @NotNull
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Size(max = 100)
    @NotNull
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "birthday")
    private LocalDate birthday;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_tier_id")
    private CustomerTier customerTier;

    @Column(name = "violation_count")
    private Integer violationCount;

    @Column(name = "restricted_until")
    private Instant restrictedUntil;

    //transient là bỏ qua cột này dưới DB, logic này chỉ chạy trên RAM
    //hàm tiện ích lấy FullName
    @Transient
    public String getFullName() {
        return (firstName == null ? "" : firstName) + " " + (lastName == null ? "" : lastName);
    }
}