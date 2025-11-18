package com.egrevs.project.gateway.service;

import com.egrevs.project.domain.entity.user.User;
import com.egrevs.project.domain.entity.user.UserHistory;
import com.egrevs.project.domain.enums.UserRole;
import com.egrevs.project.domain.repository.UserHistoryRepository;
import com.egrevs.project.domain.repository.UserRepository;
import com.egrevs.project.shared.dtos.user.CreateUserRequest;
import com.egrevs.project.shared.dtos.user.UpdateUserRequest;
import com.egrevs.project.shared.dtos.user.UserDto;
import com.egrevs.project.shared.dtos.user.UserHistoryDto;
import com.egrevs.project.shared.exceptions.user.RoleNullableException;
import com.egrevs.project.shared.exceptions.user.UserAlreadyExistsException;
import com.egrevs.project.shared.exceptions.user.UserNotFoundException;
import com.egrevs.project.shared.mapper.OrderMapper;
import com.egrevs.project.shared.mapper.ReviewMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserHistoryRepository userHistoryRepository;

    public UserService(UserRepository userRepository, UserHistoryRepository userHistoryRepository) {
        this.userRepository = userRepository;
        this.userHistoryRepository = userHistoryRepository;
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
        createNewHistoryVersion(savedUser);

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

        userHistoryRepository.closeActiveUserVersion(user.getId(), LocalDateTime.now());

        if (request.name() != null) user.setName(request.name());
        if (request.email() != null) user.setEmail(request.email());
        if (request.password() != null) user.setPassword(request.password());
        user.setUpdated_at(LocalDateTime.now());

        createNewHistoryVersion(user);

        return toDto(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        userRepository.delete(user);

        userHistoryRepository.closeActiveUserVersion(user.getId(), LocalDateTime.now());
        createDeletedVersion(user);
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

    @Transactional(readOnly = true)
    public UserHistoryDto findVersion(String userId, LocalDateTime time){
        UserHistory userHistory = userHistoryRepository.findVersionAtTime(userId, time)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " not found"));

        return toDto(userHistory);
    }

    private void createNewHistoryVersion(User user){
        UserHistory history = new UserHistory();
        history.setUserId(user.getId());
        history.setName(user.getName());
        history.setRole(user.getRole());
        history.setLogin(user.getLogin());
        history.setEmail(user.getEmail());
        history.setValidFrom(LocalDateTime.now());
        history.setValidTo(null);
        userHistoryRepository.save(history);
    }

    private void createDeletedVersion(User user) {
        UserHistory history = new UserHistory();
        history.setUserId(user.getId());
        history.setName(user.getName());
        history.setRole(user.getRole());
        history.setLogin(user.getLogin());
        history.setEmail(user.getEmail());
        history.setValidFrom(LocalDateTime.now());
        history.setValidTo(LocalDateTime.now());

        userHistoryRepository.save(history);
    }

    private UserHistoryDto toDto(UserHistory userHistory){
        return new UserHistoryDto(
                userHistory.getUserId(),
                userHistory.getName(),
                userHistory.getEmail(),
                userHistory.getLogin(),
                userHistory.getRole(),
                userHistory.getValidFrom(),
                userHistory.getValidTo()
        );
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
                user.getUpdated_at(),
                user.getReviews().stream().map(ReviewMapper::toDto).collect(Collectors.toList()),
                user.getOrderList().stream().map(OrderMapper::toDto).collect(Collectors.toList()),
                user.getCart().getId()
        );
    }

}
