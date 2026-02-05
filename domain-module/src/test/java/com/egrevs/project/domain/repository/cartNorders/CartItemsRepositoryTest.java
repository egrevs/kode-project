package com.egrevs.project.domain.repository.cartNorders;

import com.egrevs.project.domain.entity.cart.Cart;
import com.egrevs.project.domain.entity.cart.CartItems;
import com.egrevs.project.domain.entity.user.User;
import com.egrevs.project.domain.enums.UserRole;
import com.egrevs.project.domain.util.DataUtils;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CartItemsRepositoryTest {

    @Autowired
    private CartItemsRepository cartItemsRepository;

    @Autowired
    private CartsRepository cartsRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("Test deleteAllByCartId in cartItems repository functionality")
    void givenCartItemsForTwoCarts_whenDeleteAllByCartId_thenOnlyItemsOfGivenCartDeleted() {
        // given
        User user1 = DataUtils.createUser(UserRole.USER);
        User user2 = DataUtils.createUser(UserRole.USER);

        entityManager.persist(user1);
        entityManager.persist(user2);

        Cart cart1 = DataUtils.createCart(user1);
        cart1.setQuantity(2);
        cart1.setTotalPrice(BigDecimal.TEN);

        Cart cart2 = DataUtils.createCart(user2);
        cart2.setQuantity(1);
        cart2.setTotalPrice(BigDecimal.ONE);

        cartsRepository.saveAll(List.of(cart1, cart2));

        CartItems item1 = DataUtils.createCartItem(cart1);
        item1.setMenuItemName("Dish1");

        CartItems item2 = DataUtils.createCartItem(cart1);
        item2.setMenuItemName("Dish2");

        CartItems otherCartItem = DataUtils.createCartItem(cart2);
        otherCartItem.setMenuItemName("OtherDish");

        cartItemsRepository.saveAll(List.of(item1, item2, otherCartItem));

        // when
        cartItemsRepository.deleteAllByCartId(cart1.getId());

        List<CartItems> all = cartItemsRepository.findAll();

        // then
        assertThat(all)
                .hasSize(1)
                .first()
                .extracting(ci -> ci.getCart().getId())
                .isEqualTo(cart2.getId());
    }
}

