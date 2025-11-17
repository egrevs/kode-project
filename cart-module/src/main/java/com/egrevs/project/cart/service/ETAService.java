package com.egrevs.project.cart.service;

import com.egrevs.project.domain.entity.cart.Cart;
import com.egrevs.project.domain.entity.cart.CartItems;
import com.egrevs.project.domain.entity.courier.Courier;
import com.egrevs.project.domain.entity.restaurant.MenuItems;
import com.egrevs.project.domain.entity.restaurant.Restaurant;
import com.egrevs.project.domain.entity.user.User;
import com.egrevs.project.domain.enums.CourierStatus;
import com.egrevs.project.domain.repository.CourierRepository;
import com.egrevs.project.domain.repository.UserRepository;
import com.egrevs.project.domain.repository.cartNorders.CartsRepository;
import com.egrevs.project.domain.repository.restaurant.RestaurantRepository;
import com.egrevs.project.shared.exceptions.CartIsEmptyException;
import com.egrevs.project.shared.exceptions.user.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ETAService {

    public static final int DEFAULT_COOKING_MINUTES = 20;
    public static final int DEFAULT_BASE_DELIVERY_MINUTES = 15;
    public static final int MINUTES_PER_ORDER_IN_QUEUE = 8;

    private final CourierRepository courierRepository;
    private final CartsRepository cartsRepository;
    private final UserRepository userRepository;

    public Duration calculateETAForUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " not found"));

        Cart cart = cartsRepository.findByUserId(userId);
        int restaurantCookingTime = getRestaurantCookingTime(cart);

        List<Courier> availableCouriers = courierRepository.findByCourierStatus(CourierStatus.AVAILABLE);
        List<Courier> busyCouriers = courierRepository.findByCourierStatus(CourierStatus.BUSY);

        int deliveryMinutes;
        if (availableCouriers != null && !availableCouriers.isEmpty()) {
            deliveryMinutes = DEFAULT_BASE_DELIVERY_MINUTES;
        } else if (busyCouriers != null && !busyCouriers.isEmpty()) {

            double avgOrdersPerCourier = busyCouriers.stream()
                    .mapToInt(c -> c.getOrders() == null ? 0 : c.getOrders().size())
                    .average()
                    .orElse(0.0);

            deliveryMinutes = (int) (Math.round(avgOrdersPerCourier * MINUTES_PER_ORDER_IN_QUEUE)
                    + DEFAULT_BASE_DELIVERY_MINUTES);

        } else deliveryMinutes = DEFAULT_BASE_DELIVERY_MINUTES * 2;

        int totalTime = deliveryMinutes + restaurantCookingTime;

        return Duration.ofMinutes(totalTime);
    }

    private static int getRestaurantCookingTime(Cart cart) {
        if (cart.getDishes().isEmpty()) {
            throw new CartIsEmptyException("Cart is empty");
        }

        CartItems first = cart.getDishes().get(0);
        MenuItems menuItem = first.getMenuItems();

        if (menuItem == null) {
            throw new IllegalArgumentException("MenuItem is not loaded in CartItems");
        }
        Restaurant restaurant = menuItem.getRestaurant();
        int restaurantCookingTime = (restaurant != null && restaurant.getAvgCookingTime() != null)
                ? restaurant.getAvgCookingTime() : DEFAULT_COOKING_MINUTES;
        return restaurantCookingTime;
    }
}
