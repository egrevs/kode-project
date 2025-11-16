package com.egrevs.project.gateway.controller;

import com.egrevs.project.domain.enums.UserRole;
import com.egrevs.project.gateway.service.UserService;
import com.egrevs.project.shared.dtos.user.CreateUserRequest;
import com.egrevs.project.shared.dtos.user.UpdateUserRequest;
import com.egrevs.project.shared.dtos.user.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<UserDto> registerUser(@RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(userService.registerUser(request));
    }

    @Operation(summary = "Получение профиля пользователя по ID")
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(summary = "Обновление профиля по ID")
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable String id,
            @RequestBody UpdateUserRequest request
    ) {
        return ResponseEntity.ok(userService.updateUserById(request, id));
    }

    @Operation(summary = "Фильтрация по ролям пользователя")
    @GetMapping
    public ResponseEntity<List<UserDto>> filterByRole(@RequestParam(required = false) UserRole role) {
        return ResponseEntity.ok(userService.filterByRole(role));
    }

    @Operation(summary = "Деактивация пользователя")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
