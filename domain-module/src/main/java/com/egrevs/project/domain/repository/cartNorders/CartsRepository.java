package com.egrevs.project.domain.repository.cartNorders;


import com.egrevs.project.domain.entity.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartsRepository extends JpaRepository<Cart, String> {
    Cart findByUserId(String userId);
}
