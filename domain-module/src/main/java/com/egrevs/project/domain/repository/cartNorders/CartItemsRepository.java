package com.egrevs.project.domain.repository.cartNorders;

import com.egrevs.project.domain.entity.cart.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemsRepository extends JpaRepository<CartItems, String> {
    void deleteAllByCartId(String cartId);
}
