package com.egrevs.project.gateway.service;

import com.egrevs.project.domain.entity.user.User;
import com.egrevs.project.domain.enums.UserRole;
import com.egrevs.project.domain.repository.UserRepository;
import com.egrevs.project.shared.dtos.user.CreateUserRequest;
import com.egrevs.project.shared.dtos.user.UpdateUserRequest;
import com.egrevs.project.shared.dtos.user.UserDto;
import com.egrevs.project.shared.exceptions.user.RoleNullableException;
import com.egrevs.project.shared.exceptions.user.UserAlreadyExistsException;
import com.egrevs.project.shared.exceptions.user.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
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

    @Transactional
    public UserDto getUserById(String id) {
        return userRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Transactional
    public UserDto updateUserById(UpdateUserRequest request, String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));

        if (request.name() != null) user.setName(request.name());
        if (request.email() != null) user.setEmail(request.email());
        if (request.password() != null) user.setPassword(request.password());
        user.setUpdated_at(LocalDateTime.now());

        return toDto(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        userRepository.delete(user);
    }

    @Transactional
    public List<UserDto> filterByRole(UserRole role) {
        if (role == null){
            throw new RoleNullableException("Role of user can not be null");
        }
        return userRepository.findAllByRole(role)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
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
