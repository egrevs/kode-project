package com.egrevs.project.courier.service;

import com.egrevs.project.domain.entity.courier.Courier;
import com.egrevs.project.domain.entity.order.Order;
import com.egrevs.project.domain.enums.CourierStatus;
import com.egrevs.project.domain.enums.OrderStatus;
import com.egrevs.project.domain.enums.UserRole;
import com.egrevs.project.domain.repository.CourierRepository;
import com.egrevs.project.domain.repository.cartNorders.OrderRepository;
import com.egrevs.project.shared.dtos.courier.CourierDto;
import com.egrevs.project.shared.dtos.courier.CreateCourierRequest;
import com.egrevs.project.shared.dtos.courier.UpdateCourierStatusRequest;
import com.egrevs.project.shared.dtos.orders.OrderDto;
import com.egrevs.project.shared.exceptions.OrderIsNotReadyException;
import com.egrevs.project.shared.exceptions.cartNorders.OrderNotFoundException;
import com.egrevs.project.shared.exceptions.courier.CourierAlreadyExistsException;
import com.egrevs.project.shared.exceptions.courier.CourierNotFoundException;
import com.egrevs.project.shared.mapper.CourierMapper;
import com.egrevs.project.shared.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourierService {

    private final CourierRepository courierRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public CourierDto createCourier(CreateCourierRequest request) {
        if (courierRepository.findByLogin(request.login()).isPresent()) {
            throw new CourierAlreadyExistsException("Courier with login " + request.login() + " already exists");
        }

        Courier courier = new Courier();
        courier.setLogin(request.login());
        courier.setName(request.name());
        courier.setEmail(request.email());
        courier.setCourierStatus(CourierStatus.OFFLINE);
        courier.setRole(UserRole.COURIER);
        courier.setPassword(request.password());

        Courier savedCourier = courierRepository.save(courier);

        return CourierMapper.toDto(savedCourier);
    }

    @Transactional
    public List<CourierDto> getCouriers(){
        return courierRepository.findAll()
                .stream()
                .map(CourierMapper::toDto)
                .toList();
    }

    @Transactional
    public CourierDto assignOrderToCourier(String id, String orderId){
        Courier courier = courierRepository.findById(id)
                .orElseThrow(() -> new CourierNotFoundException("No courier with id " + id));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("No order with id " + orderId));

        if (order.getStatus() != OrderStatus.READY){
            throw new OrderIsNotReadyException("Order must be ready!");
        }

        order.setCourier(courier);
        courier.getOrders().add(order);
        Courier savedCourier = courierRepository.save(courier);

        return CourierMapper.toDto(savedCourier);
    }

    @Transactional
    public List<OrderDto> getActiveCourierOrders(String courierId){
        Courier courier = courierRepository.findById(courierId)
                .orElseThrow(() -> new CourierNotFoundException("No courier with id " + courierId));

        List<OrderDto> orderList = courier.getOrders().stream()
                .filter(order -> order.getStatus() == OrderStatus.DELIVERING)
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());

        return orderList;
    }

    @Transactional
    public CourierDto updateCourierStatus(UpdateCourierStatusRequest request, String courierId){
        Courier courier = courierRepository.findById(courierId)
                .orElseThrow(() -> new CourierNotFoundException("No courier with id " + courierId));

        courier.setCourierStatus(request.status());
        Courier savedCourier = courierRepository.save(courier);

        return CourierMapper.toDto(savedCourier);
    }

}
