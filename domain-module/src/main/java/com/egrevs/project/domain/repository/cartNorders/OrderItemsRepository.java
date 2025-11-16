package com.egrevs.project.domain.repository.cartNorders;

import com.egrevs.project.domain.entity.order.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemsRepository extends JpaRepository<OrderItems, String> {
}
