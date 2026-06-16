package com.swp.autocarwash.payment.entity;

import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.subscription.entity.FamilySubscription;
import com.swp.autocarwash.subscription.entity.UnlimitSubscription;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "subscription_invoice", schema = "swp_auto_car_wash")
public class SubscriptionInvoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unlimit_subscription_id")
    private UnlimitSubscription unlimitSubscription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_subscription_id")
    private FamilySubscription familySubscription;

    @NotNull
    @Column(name = "plan_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal planPrice;

    @Size(max = 20)
    @NotNull
    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "paid_at")
    private Instant paidAt;


}