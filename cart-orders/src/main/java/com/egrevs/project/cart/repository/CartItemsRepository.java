package com.egrevs.project.cart.repository;

import com.egrevs.project.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemsRepository extends JpaRepository<CartItem, String> {
    void deleteAllByCartId(String cartId);
}
