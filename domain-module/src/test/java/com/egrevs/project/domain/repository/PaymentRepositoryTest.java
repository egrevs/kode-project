package com.egrevs.project.domain.repository;

import com.egrevs.project.domain.entity.order.Order;
import com.egrevs.project.domain.entity.payment.Payment;
import com.egrevs.project.domain.entity.user.User;
import com.egrevs.project.domain.enums.OrderStatus;
import com.egrevs.project.domain.enums.PaymentMethod;
import com.egrevs.project.domain.enums.PaymentStatus;
import com.egrevs.project.domain.enums.UserRole;
import com.egrevs.project.domain.repository.cartNorders.OrderRepository;
import com.egrevs.project.domain.util.DataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Test payment save and findById functionality")
    void givenPaymentForOrder_whenSaveAndFindById_thenPaymentReturned() {
        // given
        User user = DataUtils.createUser(UserRole.USER);
        userRepository.save(user);

        Order order = DataUtils.createOrder(user, OrderStatus.PENDING);
        orderRepository.save(order);

        Payment payment = DataUtils.createPayment(order, PaymentStatus.CREATED, PaymentMethod.CARD);

        Payment saved = paymentRepository.save(payment);

        // when
        Payment found = paymentRepository.findById(saved.getId()).orElseThrow();

        // then
        assertThat(found.getOrder().getId()).isEqualTo(order.getId());
        assertThat(found.getPaymentStatus()).isEqualTo(PaymentStatus.CREATED);
        assertThat(found.getMethod()).isEqualTo(PaymentMethod.CARD);
    }
}

