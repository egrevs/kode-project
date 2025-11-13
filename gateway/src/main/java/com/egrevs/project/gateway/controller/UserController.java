package com.egrevs.project.gateway.controller;

import com.egrevs.project.gateway.dto.CreateUserRequest;
import com.egrevs.project.gateway.dto.UpdateUserRequest;
import com.egrevs.project.gateway.dto.UserDto;
import com.egrevs.project.gateway.entity.UserRole;
import com.egrevs.project.gateway.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDto> registerUser(
            @RequestBody CreateUserRequest request,
            @RequestParam("role") UserRole role) {
        UserDto userDto = userService.registerUser(request, role);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<UserDto> findById(@PathVariable String id) {
//        UserDto userDto = userService.getUserById(id);
//        return ResponseEntity.ok(userDto);
//    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable String id,
            @RequestBody UpdateUserRequest request
    ) {
        UserDto updatedUser = userService.updateUserById(request, id);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> filterByRole(@RequestParam(required = false) UserRole role) {
        if (role != null) {
            return ResponseEntity.ok(userService.filterByRole(role));
        }else{
            return ResponseEntity.ok(userService.getAll());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable String id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
