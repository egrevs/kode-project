package com.egrevs.project.shared.mapper;

import com.egrevs.project.domain.entity.order.Order;
import com.egrevs.project.domain.entity.order.OrderItems;
import com.egrevs.project.shared.dtos.orders.OrderDto;
import com.egrevs.project.shared.dtos.orders.OrderItemsDto;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public static OrderDto toDto(Order order) {
        return new OrderDto(
                order.getId(),
                order.getUserId(),
                order.getStatus(),
                order.getTotalPrice(),
                order.getCreatedAt(),
                order.getItems().stream().map(OrderMapper::toDto).toList());
    }

    public static OrderItemsDto toDto(OrderItems orderItems){
        return new OrderItemsDto(
                orderItems.getId(),
                orderItems.getDishId(),
                orderItems.getDishName(),
                orderItems.getQuantity(),
                orderItems.getDishPrice(),
                orderItems.getTotalPrice(),
                orderItems.getCreatedAt()
        );
    }
}
