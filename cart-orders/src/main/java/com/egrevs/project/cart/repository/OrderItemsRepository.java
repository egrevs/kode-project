package com.egrevs.project.cart.repository;

import com.egrevs.project.cart.entity.order.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemsRepository extends JpaRepository<OrderItems, String> {
}
