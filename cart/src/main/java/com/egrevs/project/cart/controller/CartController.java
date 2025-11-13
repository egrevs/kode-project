package com.egrevs.project.cart.controller;

import com.egrevs.project.cart.service.CartService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/cart")
public class CartController {

    private CartService cartService;


}
