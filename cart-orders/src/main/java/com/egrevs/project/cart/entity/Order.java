package com.egrevs.project.cart.entity;

import com.egrevs.project.cart.dto.orders.OrderStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "order")
public class Order {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "id")
    private String id;

    @Column(name = "userId")
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "cart_id")
    private String cartId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
