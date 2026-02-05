package com.egrevs.project.domain.repository.cartNorders;

import com.egrevs.project.domain.entity.order.OrderItems;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OrderItemsRepositoryTest {

    @Autowired
    private OrderItemsRepository orderItemsRepository;

    @Test
    @DisplayName("Test order item save and findById functionality")
    void givenOrderItem_whenSaveAndFindById_thenOrderItemReturned() {
        // given
        OrderItems orderItem = new OrderItems();
        orderItem.setMenuItemId("menu-1");
        orderItem.setMenuItemName("Pizza");
        orderItem.setQuantity(2);
        orderItem.setMenuItemPrice(BigDecimal.valueOf(5));
        orderItem.setTotalPrice(BigDecimal.valueOf(10));
        orderItem.setCreatedAt(LocalDateTime.now());

        // when
        OrderItems saved = orderItemsRepository.save(orderItem);

        // then
        assertThat(saved.getId()).isNotNull();
        assertThat(orderItemsRepository.findById(saved.getId())).isPresent();
    }
}

