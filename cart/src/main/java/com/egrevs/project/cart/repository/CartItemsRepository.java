package com.egrevs.project.cart.repository;

import com.egrevs.project.cart.entity.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemsRepository extends JpaRepository<CartItems, Long> {
    void deleteAllByCartId(Long cartId);
}
