package com.egrevs.project.cart.dto.orders;

public enum OrderStatus {
    PENDING, // ОЖИДАЕТ ПОДТВЕРЖДЕНИЯ
    COLLECTING, // СОБИРАЕТСЯ
    SHIPPED, // В ДОСТАВКЕ
    DELIVERED, // ДОСТАВЛЕН
}
