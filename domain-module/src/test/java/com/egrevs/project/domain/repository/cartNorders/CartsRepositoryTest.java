package com.egrevs.project.domain.repository.cartNorders;

import com.egrevs.project.domain.entity.cart.Cart;
import com.egrevs.project.domain.entity.user.User;
import com.egrevs.project.domain.enums.UserRole;
import com.egrevs.project.domain.util.DataUtils;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CartsRepositoryTest {

    @Autowired
    private CartsRepository cartsRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("Test findByUserId in carts repository functionality")
    void givenCartForUser_whenFindByUserId_thenCartReturned() {
        // given
        User user = DataUtils.createUser(UserRole.USER);
        entityManager.persist(user);

        Cart cart = DataUtils.createCart(user);
        cart.setQuantity(2);
        cart.setTotalPrice(BigDecimal.TEN);

        cartsRepository.save(cart);

        // when
        Cart result = cartsRepository.findByUserId(user.getId());

        // then
        assertThat(result).isNotNull();
        assertThat(result.getUser().getId()).isEqualTo(user.getId());
        assertThat(result.getQuantity()).isEqualTo(2);
    }
}

