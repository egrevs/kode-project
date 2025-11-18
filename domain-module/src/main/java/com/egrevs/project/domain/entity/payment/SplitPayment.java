package com.egrevs.project.domain.entity.payment;

import com.egrevs.project.domain.entity.restaurant.Restaurant;
import com.egrevs.project.domain.enums.PaymentMethod;
import com.egrevs.project.domain.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "split_payments")
@Getter
@Setter
public class SplitPayment {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "id")
    private String id;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Column(name = "status")
    private PaymentStatus status;

    @Column(name = "method")
    private PaymentMethod method;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;
}
