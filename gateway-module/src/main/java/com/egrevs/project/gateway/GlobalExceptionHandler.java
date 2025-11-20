package com.egrevs.project.gateway;

import com.egrevs.project.shared.exceptions.*;
import com.egrevs.project.shared.exceptions.cartNorders.CartNotFoundException;
import com.egrevs.project.shared.exceptions.cartNorders.OrderIsEmptyException;
import com.egrevs.project.shared.exceptions.cartNorders.OrderNotFoundException;
import com.egrevs.project.shared.exceptions.courier.CourierAlreadyExistsException;
import com.egrevs.project.shared.exceptions.courier.CourierNotFoundException;
import com.egrevs.project.shared.exceptions.restaurant.DishNotFoundException;
import com.egrevs.project.shared.exceptions.restaurant.RestaurantIsAlreadyExistsException;
import com.egrevs.project.shared.exceptions.restaurant.RestaurantNotFoundException;
import com.egrevs.project.shared.exceptions.user.RoleNullableException;
import com.egrevs.project.shared.exceptions.user.UserAlreadyExistsException;
import com.egrevs.project.shared.exceptions.user.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(basePackages = "com.egrevs.project")
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFound(UserNotFoundException e) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleUserAlreadyExists(UserAlreadyExistsException e) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(RoleNullableException.class)
    public ResponseEntity<Map<String, Object>> handleRoleNullable(RoleNullableException e) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(RestaurantNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleRestaurantNotFound(RestaurantNotFoundException e) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(RestaurantIsAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleRestaurantAlreadyExists(RestaurantIsAlreadyExistsException e) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(DishNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleDishNotFound(DishNotFoundException e) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleCartNotFound(CartNotFoundException e) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleOrderNotFound(OrderNotFoundException e) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(OrderIsEmptyException.class)
    public ResponseEntity<Map<String, Object>> handleOrderIsEmpty(OrderIsEmptyException e) {
        return buildErrorResponse(HttpStatus.NO_CONTENT, e.getMessage());
    }

    @ExceptionHandler(CourierNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleCourierNotFound(CourierNotFoundException e) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(CourierAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleCourierAlreadyExists(CourierAlreadyExistsException e) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(ReviewNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleReviewNotFound(ReviewNotFoundException e) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handlePaymentNotFound(PaymentNotFoundException e){
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(NotificationNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotificationNotFound(NotificationNotFoundException e){
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(CartIsEmptyException.class)
    public ResponseEntity<Map<String, Object>> handleCartIsEmpty(CartIsEmptyException e){
        return buildErrorResponse(HttpStatus.NO_CONTENT, e.getMessage());
    }

    @ExceptionHandler(InvalidOrderPriceException.class)
    public ResponseEntity<Map<String, Object>> handleOrderPriceException(InvalidOrderPriceException e){
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(OrderWithoutItemsException.class)
    public ResponseEntity<Map<String, Object>> handleOrderItemsException(OrderWithoutItemsException e){
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(OrderIsNotReadyException.class)
    public ResponseEntity<Map<String, Object>> handleOrderReadyException(OrderIsNotReadyException e){
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(OrderNotCancelledException.class)
    public ResponseEntity<Map<String, Object>> handleOrderCancelException(OrderNotCancelledException e){
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception e) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "Произошла внутренняя ошибка сервера: " + e.getMessage());
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String message) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", status.value());
        errorResponse.put("error", status.getReasonPhrase());
        errorResponse.put("message", message);
        return ResponseEntity.status(status).body(errorResponse);
    }
}

