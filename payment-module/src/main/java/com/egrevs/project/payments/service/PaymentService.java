package com.egrevs.project.payments.service;

import com.egrevs.project.domain.entity.order.Order;
import com.egrevs.project.domain.entity.payment.Payment;
import com.egrevs.project.domain.enums.PaymentStatus;
import com.egrevs.project.domain.repository.PaymentRepository;
import com.egrevs.project.domain.repository.cartNorders.OrderRepository;
import com.egrevs.project.shared.dtos.payments.CreatePaymentRequest;
import com.egrevs.project.shared.dtos.payments.PaymentDto;
import com.egrevs.project.shared.dtos.payments.UpdatePaymentRequest;
import com.egrevs.project.shared.exceptions.PaymentNotFoundException;
import com.egrevs.project.shared.exceptions.cartNorders.OrderNotFoundException;
import com.egrevs.project.shared.mapper.PaymentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    //TODO добавить split payments
    @Transactional
    public PaymentDto createPayment(CreatePaymentRequest request){
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
    public PaymentStatus getPaymentStatus(String id){
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment with id " + id + " not found"));

        return payment.getPaymentStatus();
    }

    @Transactional
    public PaymentDto updatePaymentStatus(String id, UpdatePaymentRequest request){
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment with id " + id + " not found"));

        if (request.status() != null) payment.setPaymentStatus(request.status());
        Payment savedPayment = paymentRepository.save(payment);

        return PaymentMapper.toDto(savedPayment);
    }

    @Transactional
    public List<PaymentDto> getPaymentByOrder(String orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with id " + orderId + " not found"));

        return order.getPayments()
                .stream()
                .map(PaymentMapper::toDto)
                .collect(Collectors.toList());
    }

}
