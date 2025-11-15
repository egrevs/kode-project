package com.egrevs.project.cart.service;

import com.egrevs.project.cart.dto.orders.*;
import com.egrevs.project.cart.entity.cart.Cart;
import com.egrevs.project.cart.entity.order.Order;
import com.egrevs.project.cart.entity.order.OrderItems;
import com.egrevs.project.cart.exception.CartNotFoundException;
import com.egrevs.project.cart.exception.OrderIsEmptyException;
import com.egrevs.project.cart.exception.OrderNotFoundException;
import com.egrevs.project.cart.repository.CartsRepository;
import com.egrevs.project.cart.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartsRepository cartsRepository;
    private final OrderRepository orderRepository;

    public OrderDto createOrderWithCart(CreateOrderRequest request){
        Cart cart = cartsRepository.findById(request.cartId())
                .orElseThrow(() -> new CartNotFoundException("No cart with id " + request.cartId()));

        Order order = new Order();
        order.setUserId(request.userId());
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        List<OrderItems> items = cart.getDishes().stream().map(cartItems -> {
            OrderItems orderItems = new OrderItems();
            orderItems.setDishId(cartItems.getDishId());
            orderItems.setDishName(cartItems.getDishName());
            orderItems.setCreatedAt(LocalDateTime.now());
            orderItems.setQuantity(cartItems.getQuantity());
            orderItems.setTotalPrice(cartItems.getTotalPrice());
            orderItems.setDishPrice(cartItems.getDishPrice());
            orderItems.setOrder(order);
            return orderItems;
        }).collect(Collectors.toList());

        order.setTotalPrice(items.stream()
                .map(OrderItems::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        order.setItems(items);
        Order savedOrder = orderRepository.save(order);

        return toDto(savedOrder);
    }

    public OrderDto getOrder(String id){
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("No order with id " + id));

        return toDto(order);
    }

    public List<OrderDto> getUserOrders(String userId){
        List<Order> orderList = orderRepository.findAllByUserId(userId);
        if (orderList.isEmpty()){
            throw new OrderIsEmptyException("User with id " + userId + " doesn't have orders");
        }

        return orderRepository.findAllByUserId(userId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public OrderDto changeOrderStatus(UpdateOrderStatusRequest request, String id){
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("No order with id " + id));

        order.setStatus(request.status());
        Order savedOrder = orderRepository.save(order);
        return toDto(savedOrder);
    }

    public void cancelOrder(String orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("No order with id " + orderId));

        orderRepository.delete(order);
    }

    public List<OrderDto> filterByStatus(OrderStatus status){
        return orderRepository.findAllByStatus(status)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private OrderDto toDto(Order order){
        return new OrderDto(
                order.getId(),
                order.getUserId(),
                order.getStatus(),
                order.getTotalPrice(),
                order.getCreatedAt(),
                order.getItems().stream().map(this::toDto).toList()
        );
    }

    private OrderItemsDto toDto(OrderItems orderItems){
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
