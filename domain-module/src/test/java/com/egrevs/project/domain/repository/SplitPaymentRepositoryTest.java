package com.egrevs.project.domain.repository;

import com.egrevs.project.domain.entity.order.Order;
import com.egrevs.project.domain.entity.payment.Payment;
import com.egrevs.project.domain.entity.payment.SplitPayment;
import com.egrevs.project.domain.entity.restaurant.Restaurant;
import com.egrevs.project.domain.entity.user.User;
import com.egrevs.project.domain.enums.*;
import com.egrevs.project.domain.repository.cartNorders.OrderRepository;
import com.egrevs.project.domain.repository.restaurant.RestaurantRepository;
import com.egrevs.project.domain.util.DataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class SplitPaymentRepositoryTest {

    @Autowired
    private SplitPaymentRepository splitPaymentRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    @DisplayName("Test splitPayment save and findById functionality")
    void givenSplitPaymentForPaymentAndRestaurant_whenSaveAndFindById_thenSplitPaymentReturned() {
        // given
        User user = DataUtils.createUser(UserRole.USER);
        userRepository.save(user);

        Order order = DataUtils.createOrder(user, OrderStatus.PENDING);
        orderRepository.save(order);

        Payment payment = DataUtils.createPayment(order, PaymentStatus.CREATED, PaymentMethod.CARD);
        paymentRepository.save(payment);

        Restaurant restaurant = DataUtils.createRestaurant(RestaurantCuisine.JAPANESE);
        restaurantRepository.save(restaurant);

        SplitPayment splitPayment = DataUtils.createSplitPayment(
                payment,
                restaurant,
                PaymentStatus.CREATED,
                PaymentMethod.CARD
        );

        SplitPayment saved = splitPaymentRepository.save(splitPayment);

        // when
        SplitPayment found = splitPaymentRepository.findById(saved.getId()).orElseThrow();

        // then
        assertThat(found.getPayment().getId()).isEqualTo(payment.getId());
        assertThat(found.getRestaurant().getId()).isEqualTo(restaurant.getId());
        assertThat(found.getStatus()).isEqualTo(PaymentStatus.CREATED);
        assertThat(found.getMethod()).isEqualTo(PaymentMethod.CARD);
    }
}

