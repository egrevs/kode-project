package com.egrevs.project.gateway.service;

import com.egrevs.project.gateway.dto.CreateUserRequest;
import com.egrevs.project.gateway.dto.UpdateUserRequest;
import com.egrevs.project.gateway.dto.UserDto;
import com.egrevs.project.gateway.entity.User;
import com.egrevs.project.gateway.entity.UserRole;
import com.egrevs.project.gateway.exceptions.RoleNullableException;
import com.egrevs.project.gateway.exceptions.UserAlreadyExistsException;
import com.egrevs.project.gateway.exceptions.UserNotFoundException;
import com.egrevs.project.gateway.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto registerUser(CreateUserRequest request) {
        if (userRepository.existsByLogin(request.login())) {
            throw new UserAlreadyExistsException("Such username is already registered!");
        }

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setCreated_at(LocalDateTime.now());
        user.setLogin(request.login());
        user.setPassword(request.password());
        user.setRole(UserRole.USER);

        User savedUser = userRepository.save(user);

        return toDto(savedUser);
    }

    public UserDto getUserById(String id) {
        return userRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public UserDto updateUserById(UpdateUserRequest request, String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));

        if (request.name() != null) user.setName(request.name());
        if (request.email() != null) user.setEmail(request.email());
        if (request.password() != null) user.setPassword(request.password());
        user.setUpdated_at(LocalDateTime.now());

        return toDto(userRepository.save(user));
    }

    public void deleteUser(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        userRepository.delete(user);
    }

    public List<UserDto> filterByRole(UserRole role) {
        if (role == null){
            throw new RoleNullableException("Role of user can not be null");
        }
        return userRepository.findAllByRole(role)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public List<UserDto> getAll() {
        return userRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    private UserDto toDto(User user) {
        if (user == null) {
            return null;
        }

        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getRole(),
                user.getCreated_at(),
                user.getUpdated_at()
        );
    }

}
