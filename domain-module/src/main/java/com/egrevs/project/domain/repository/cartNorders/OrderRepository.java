package com.egrevs.project.domain.repository.cartNorders;

import com.egrevs.project.domain.entity.order.Order;
import com.egrevs.project.domain.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findAllByUserId(String userId);

    List<Order> findAllByStatus(OrderStatus status);
}
