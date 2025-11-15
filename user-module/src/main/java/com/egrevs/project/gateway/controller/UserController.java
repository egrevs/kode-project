package com.egrevs.project.gateway.controller;

import com.egrevs.project.gateway.dto.CreateUserRequest;
import com.egrevs.project.gateway.dto.UpdateUserRequest;
import com.egrevs.project.gateway.dto.UserDto;
import com.egrevs.project.gateway.entity.UserRole;
import com.egrevs.project.gateway.exceptions.RoleNullableException;
import com.egrevs.project.gateway.exceptions.UserAlreadyExistsException;
import com.egrevs.project.gateway.exceptions.UserNotFoundException;
import com.egrevs.project.gateway.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Регистрация пользователя")
    @PostMapping
    public ResponseEntity<UserDto> registerUser(
            @RequestBody CreateUserRequest request,
            @RequestParam("role") UserRole role) {
        try {
            return ResponseEntity.ok(userService.registerUser(request, role));
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Получение профиля пользователя по ID")
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable String id) {
        try {
            return ResponseEntity.ok(userService.getUserById(id));
        }catch (UserNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Обновление профиля по ID")
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable String id,
            @RequestBody UpdateUserRequest request
    ) {
        try {
            return  ResponseEntity.ok(userService.updateUserById(request, id));
        } catch (UserNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Фильтрация по ролям пользователя")
    @GetMapping
    public ResponseEntity<List<UserDto>> filterByRole(@RequestParam(required = false) UserRole role) {
        try {
            return ResponseEntity.ok(userService.filterByRole(role));
        } catch (RoleNullableException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Деактивация пользователя")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable String id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (UserNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }
}
