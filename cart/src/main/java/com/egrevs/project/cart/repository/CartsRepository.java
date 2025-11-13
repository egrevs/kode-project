package com.egrevs.project.cart.repository;

import com.egrevs.project.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartsRepository extends JpaRepository<Cart, String> {
}
