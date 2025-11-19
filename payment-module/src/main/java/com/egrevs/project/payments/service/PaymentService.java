package com.egrevs.project.payments.service;

import com.egrevs.project.domain.entity.order.Order;
import com.egrevs.project.domain.entity.order.OrderItems;
import com.egrevs.project.domain.entity.payment.Payment;
import com.egrevs.project.domain.entity.payment.SplitPayment;
import com.egrevs.project.domain.entity.restaurant.MenuItems;
import com.egrevs.project.domain.enums.PaymentStatus;
import com.egrevs.project.domain.repository.PaymentRepository;
import com.egrevs.project.domain.repository.cartNorders.OrderRepository;
import com.egrevs.project.domain.repository.restaurant.MenuItemsRepository;
import com.egrevs.project.shared.dtos.payments.*;
import com.egrevs.project.shared.exceptions.PaymentNotFoundException;
import com.egrevs.project.shared.exceptions.cartNorders.OrderNotFoundException;
import com.egrevs.project.shared.mapper.PaymentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final MenuItemsRepository menuItemsRepository;

    @Transactional
    public PaymentDto createPayment(CreatePaymentRequest request) {
        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new OrderNotFoundException("Order with id " + request.orderId() + " not found"));

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentStatus(request.status());
        payment.setMethod(request.method());
        payment.setTotalAmount(order.getTotalPrice());
        payment.setCreatedAt(LocalDateTime.now());

        Payment savedPayment = paymentRepository.save(payment);

        return PaymentMapper.toDto(savedPayment);
    }

    @Transactional
    public List<SplitPaymentDto> createSplitPayments(String paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found"));

        Order order = payment.getOrder();
        List<String> menuItemIds = order.getItems().stream()
                .map(OrderItems::getMenuItemId)
                .toList();

        Map<String, MenuItems> menuItemsMap = menuItemsRepository.findAllById(menuItemIds)
                .stream().collect(Collectors.toMap(MenuItems::getId, m -> m));

        Map<String, BigDecimal> amountPerRestaurant = order.getItems().stream()
                .collect(Collectors.groupingBy(
                        item -> menuItemsMap.get(item.getMenuItemId()).getRestaurant().getId(),
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                OrderItems::getTotalPrice,
                                BigDecimal::add
                        )
                ));

        List<SplitPayment> splits = amountPerRestaurant.entrySet().stream()
                .map(entry -> {
                    SplitPayment split = new SplitPayment();
                    split.setPayment(payment);
                    split.setPrice(entry.getValue());
                    split.setStatus(PaymentStatus.PENDING);
                    split.setCreatedAt(LocalDateTime.now());
                    split.setUpdatedAt(LocalDateTime.now());

                    split.setRestaurant(menuItemsMap.values().stream()
                            .filter(m -> m.getRestaurant().getId().equals(entry.getKey()))
                            .findFirst()
                            .get()
                            .getRestaurant());

                    return split;
                }).toList();

        payment.setSplitPayments(splits);
        paymentRepository.save(payment);

        return splits.stream().map(this::toDto).toList();
    }

    @Transactional
    public PaymentStatus getPaymentStatus(String id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment with id " + id + " not found"));

        return payment.getPaymentStatus();
    }

    @Transactional
    public PaymentDto updatePaymentStatus(String id, UpdatePaymentRequest request) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment with id " + id + " not found"));

        if (request.status() != null) payment.setPaymentStatus(request.status());
        Payment savedPayment = paymentRepository.save(payment);

        return PaymentMapper.toDto(savedPayment);
    }

    @Transactional
    public List<PaymentDto> getPaymentByOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with id " + orderId + " not found"));

        return order.getPayments()
                .stream()
                .map(PaymentMapper::toDto)
                .collect(Collectors.toList());
    }

    private SplitPaymentDto toDto(SplitPayment split) {
        return new SplitPaymentDto(
                split.getId(),
                split.getPayment().getId(),
                split.getRestaurant().getId(),
                split.getStatus(),
                split.getPayment().getMethod(),
                split.getPrice(),
                split.getCreatedAt(),
                split.getUpdatedAt()
        );
    }

}
