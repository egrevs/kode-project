package com.egrevs.project.domain.repository.cartNorders;

import com.egrevs.project.domain.entity.order.Order;
import com.egrevs.project.domain.entity.user.User;
import com.egrevs.project.domain.enums.OrderStatus;
import com.egrevs.project.domain.enums.UserRole;
import com.egrevs.project.domain.util.DataUtils;
import org.junit.jupiter.api.DisplayName;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("Test findAllByUserId in order repository functionality")
    void givenOrdersForTwoUsers_whenFindAllByUserId_thenReturnOnlyOrdersOfGivenUser() {
        // given
        User user1 = DataUtils.createUser(UserRole.USER);
        User user2 = DataUtils.createUser(UserRole.USER);
        entityManager.persist(user1);
        entityManager.persist(user2);

        Order order1 = DataUtils.createOrder(user1, OrderStatus.PENDING);
        order1.setTotalPrice(BigDecimal.TEN);

        Order order2 = DataUtils.createOrder(user1, OrderStatus.PENDING);
        order2.setTotalPrice(BigDecimal.ONE);

        Order otherUserOrder = DataUtils.createOrder(user2, OrderStatus.PENDING);
        otherUserOrder.setTotalPrice(BigDecimal.ONE);

        orderRepository.saveAll(List.of(order1, order2, otherUserOrder));

        // when
        List<Order> result = orderRepository.findAllByUserId(user1.getId());

        // then
        assertThat(result)
                .hasSize(2)
                .allMatch(o -> o.getUser().getId().equals(user1.getId()));
    }

    @Test
    @DisplayName("Test findAllByStatus in order repository functionality")
    void givenOrdersWithDifferentStatuses_whenFindAllByStatus_thenReturnOnlyOrdersWithGivenStatus() {
        // given
        User user = DataUtils.createUser(UserRole.USER);
        entityManager.persist(user);

        Order pendingOrder = DataUtils.createOrder(user, OrderStatus.PENDING);
        pendingOrder.setTotalPrice(BigDecimal.TEN);

        Order deliveredOrder = DataUtils.createOrder(user, OrderStatus.DELIVERED);
        deliveredOrder.setTotalPrice(BigDecimal.ONE);

        orderRepository.saveAll(List.of(pendingOrder, deliveredOrder));

        // when
        List<Order> result = orderRepository.findAllByStatus(OrderStatus.PENDING);

        // then
        assertThat(result)
                .hasSize(1)
                .first()
                .extracting(Order::getStatus)
                .isEqualTo(OrderStatus.PENDING);
    }
}

