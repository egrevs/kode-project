package com.egrevs.project.order.service;

import com.egrevs.project.domain.entity.cart.Cart;
import com.egrevs.project.domain.entity.order.Order;
import com.egrevs.project.domain.entity.order.OrderItems;
import com.egrevs.project.domain.entity.user.User;
import com.egrevs.project.domain.enums.OrderStatus;
import com.egrevs.project.domain.repository.UserRepository;
import com.egrevs.project.domain.repository.cartNorders.CartsRepository;
import com.egrevs.project.domain.repository.cartNorders.OrderRepository;
import com.egrevs.project.shared.dtos.orders.CreateOrderRequest;
import com.egrevs.project.shared.dtos.orders.OrderDto;
import com.egrevs.project.shared.dtos.orders.UpdateOrderStatusRequest;
import com.egrevs.project.shared.exceptions.InvalidOrderPriceException;
import com.egrevs.project.shared.exceptions.OrderNotCancelledException;
import com.egrevs.project.shared.exceptions.OrderWithoutItemsException;
import com.egrevs.project.shared.exceptions.cartNorders.CartNotFoundException;
import com.egrevs.project.shared.exceptions.cartNorders.OrderIsEmptyException;
import com.egrevs.project.shared.exceptions.cartNorders.OrderNotFoundException;
import com.egrevs.project.shared.exceptions.user.UserNotFoundException;
import com.egrevs.project.shared.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartsRepository cartsRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Transactional
    public OrderDto createOrderWithCart(CreateOrderRequest request){
        Cart cart = cartsRepository.findById(request.cartId())
                .orElseThrow(() -> new CartNotFoundException("No cart with id " + request.cartId()));

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new UserNotFoundException("User with id " + request.userId() + " not found"));

        Order order = new Order();
        order.setUser(user);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        List<OrderItems> items = cart.getDishes().stream().map(cartItems -> {
            OrderItems orderItems = new OrderItems();
            orderItems.setMenuItemId(cartItems.getMenuItems().getId());
            orderItems.setMenuItemName(cartItems.getMenuItemName());
            orderItems.setCreatedAt(LocalDateTime.now());
            orderItems.setQuantity(cartItems.getQuantity());
            orderItems.setTotalPrice(cartItems.getTotalPrice());
            orderItems.setMenuItemPrice(cartItems.getMenuItemPrice());
            orderItems.setOrder(order);
            return orderItems;
        }).collect(Collectors.toList());

        order.setTotalPrice(items.stream()
                .map(OrderItems::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        if (order.getTotalPrice().compareTo(new BigDecimal("300")) < 0){
            throw new InvalidOrderPriceException("Order price must be more than 300");
        }

        if (items.isEmpty()){
            throw new OrderWithoutItemsException("Order can not be created without items");
        }

        order.setItems(items);
        Order savedOrder = orderRepository.save(order);

        return OrderMapper.toDto(savedOrder);
    }

    @Transactional
    public OrderDto getOrder(String id){
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("No order with id " + id));

        return OrderMapper.toDto(order);
    }

    @Transactional
    public List<OrderDto> getUserOrders(String userId){
        List<Order> orderList = orderRepository.findAllByUserId(userId);
        if (orderList.isEmpty()){
            throw new OrderIsEmptyException("User with id " + userId + " doesn't have orders");
        }

        return orderRepository.findAllByUserId(userId)
                .stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderDto changeOrderStatus(UpdateOrderStatusRequest request, String id){
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("No order with id " + id));

        order.setStatus(request.status());
        Order savedOrder = orderRepository.save(order);
        return OrderMapper.toDto(savedOrder);
    }

    public void cancelOrder(String orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("No order with id " + orderId));

        if (order.getStatus() == OrderStatus.DELIVERED){
            throw new OrderNotCancelledException("Order can not be cancelled");
        }

        orderRepository.delete(order);
    }

    @Transactional
    public List<OrderDto> filterByStatus(OrderStatus status){
        return orderRepository.findAllByStatus(status)
                .stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
    }
}
