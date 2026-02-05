package com.egrevs.project.domain.util;

import com.egrevs.project.domain.entity.cart.Cart;
import com.egrevs.project.domain.entity.cart.CartItems;
import com.egrevs.project.domain.entity.courier.Courier;
import com.egrevs.project.domain.entity.notification.Notification;
import com.egrevs.project.domain.entity.order.Order;
import com.egrevs.project.domain.entity.order.OrderItems;
import com.egrevs.project.domain.entity.payment.Payment;
import com.egrevs.project.domain.entity.payment.SplitPayment;
import com.egrevs.project.domain.entity.restaurant.MenuItems;
import com.egrevs.project.domain.entity.restaurant.MenuItemsVariant;
import com.egrevs.project.domain.entity.restaurant.Restaurant;
import com.egrevs.project.domain.entity.review.Review;
import com.egrevs.project.domain.entity.user.User;
import com.egrevs.project.domain.entity.user.UserHistory;
import com.egrevs.project.domain.enums.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class DataUtils {

    private DataUtils() {
    }

    public static User createUser(UserRole role) {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test.user@example.com");
        user.setLogin("test_user");
        user.setPassword("password");
        user.setRole(role);
        user.setCreated_at(LocalDateTime.now());
        user.setUpdated_at(LocalDateTime.now());
        return user;
    }

    public static Courier createCourier(CourierStatus status) {
        Courier courier = new Courier();
        courier.setName("Test Courier");
        courier.setLogin("courier_login");
        courier.setEmail("courier@example.com");
        courier.setPassword("password");
        courier.setRole(UserRole.COURIER);
        courier.setCourierStatus(status);
        courier.setCreated_at(LocalDateTime.now());
        return courier;
    }

    public static Restaurant createRestaurant(RestaurantCuisine cuisine) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        restaurant.setCuisine(cuisine);
        restaurant.setRating(4.5f);
        restaurant.setAvgCookingTime(30);
        // createdAt / updatedAt инициализируются в самом entity
        return restaurant;
    }

    public static MenuItems createMenuItem(Restaurant restaurant) {
        MenuItems menuItems = new MenuItems();
        menuItems.setRestaurant(restaurant);
        menuItems.setName("Test Dish");
        menuItems.setPrice(BigDecimal.TEN);
        menuItems.setIsAvailable(true);
        menuItems.setCreatedAt(LocalDateTime.now());
        menuItems.setUpdatedAt(LocalDateTime.now());
        return menuItems;
    }

    public static MenuItemsVariant createMenuItemVariant(MenuItems menuItems, VariantSize size) {
        MenuItemsVariant variant = new MenuItemsVariant();
        variant.setMenuItems(menuItems);
        variant.setSize(size);
        variant.setPrice(BigDecimal.ONE);
        variant.setPreparationTime(10);
        return variant;
    }

    public static Cart createCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setQuantity(0);
        cart.setTotalPrice(BigDecimal.ZERO);
        cart.setCreatedAt(LocalDateTime.now());
        cart.setUpdatedAt(LocalDateTime.now());
        return cart;
    }

    public static CartItems createCartItem(Cart cart) {
        CartItems cartItems = new CartItems();
        cartItems.setCart(cart);
        cartItems.setMenuItemName("Test Item");
        cartItems.setMenuItemPrice(BigDecimal.ONE);
        cartItems.setTotalPrice(BigDecimal.ONE);
        cartItems.setQuantity(1);
        cartItems.setCreatedAt(LocalDateTime.now());
        return cartItems;
    }

    public static Order createOrder(User user, OrderStatus status) {
        Order order = new Order();
        order.setUser(user);
        order.setStatus(status);
        order.setTotalPrice(BigDecimal.TEN);
        order.setCreatedAt(LocalDateTime.now());
        return order;
    }

    public static OrderItems createOrderItem(Order order) {
        OrderItems orderItems = new OrderItems();
        orderItems.setOrder(order);
        orderItems.setMenuItemId("menu-1");
        orderItems.setMenuItemName("Order Item");
        orderItems.setQuantity(1);
        orderItems.setMenuItemPrice(BigDecimal.ONE);
        orderItems.setTotalPrice(BigDecimal.ONE);
        orderItems.setCreatedAt(LocalDateTime.now());
        return orderItems;
    }

    public static Payment createPayment(Order order,
                                        PaymentStatus status,
                                        PaymentMethod method) {
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setTotalAmount(BigDecimal.TEN);
        payment.setPaymentStatus(status);
        payment.setMethod(method);
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());
        return payment;
    }

    public static SplitPayment createSplitPayment(Payment payment,
                                                  Restaurant restaurant,
                                                  PaymentStatus status,
                                                  PaymentMethod method) {
        SplitPayment splitPayment = new SplitPayment();
        splitPayment.setPayment(payment);
        splitPayment.setRestaurant(restaurant);
        splitPayment.setStatus(status);
        splitPayment.setMethod(method);
        splitPayment.setPrice(BigDecimal.ONE);
        splitPayment.setCreatedAt(LocalDateTime.now());
        splitPayment.setUpdatedAt(LocalDateTime.now());
        return splitPayment;
    }

    public static Notification createNotification(User user, NotificationStatus status) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage("Test notification");
        notification.setStatus(status);
        notification.setCreatedAt(LocalDateTime.now());
        return notification;
    }

    public static Review createReview(User user, Restaurant restaurant) {
        Review review = new Review();
        review.setUser(user);
        review.setRestaurant(restaurant);
        review.setText("Nice restaurant");
        review.setRating(4.0f);
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());
        return review;
    }

    public static UserHistory createUserHistory(String userId,
                                                UserRole role,
                                                LocalDateTime validFrom,
                                                LocalDateTime validTo) {
        UserHistory history = new UserHistory();
        history.setUserId(userId);
        history.setName("History User");
        history.setEmail("history@example.com");
        history.setLogin("history_login");
        history.setRole(role);
        history.setValidFrom(validFrom);
        history.setValidTo(validTo);
        return history;
    }
}

