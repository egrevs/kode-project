package com.egrevs.project.gateway.controller;

import com.egrevs.project.domain.enums.UserRole;
import com.egrevs.project.gateway.service.UserService;
import com.egrevs.project.shared.dtos.user.CreateUserRequest;
import com.egrevs.project.shared.dtos.user.UpdateUserRequest;
import com.egrevs.project.shared.dtos.user.UserDto;
import com.egrevs.project.shared.dtos.user.UserHistoryDto;
import com.egrevs.project.shared.exceptions.user.RoleNullableException;
import com.egrevs.project.shared.exceptions.user.UserAlreadyExistsException;
import com.egrevs.project.shared.exceptions.user.UserNotFoundException;
import com.egrevs.project.utils.DataUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Test register user functionality")
    void givenUserToRegister_whenRegister_thenSuccessResponse() throws Exception {
        //given
        CreateUserRequest userRequest = DataUtils.createUserRequest();
        UserDto userDto = new UserDto(
                "123",
                userRequest.name(),
                userRequest.email(),
                userRequest.login(),
                UserRole.USER,
                LocalDateTime.now(),
                LocalDateTime.now(),
                List.of(),
                List.of(),
                "cart-456"
        );
        BDDMockito.given(userService.registerUser(userRequest))
                .willReturn(userDto);
        //when
        mockMvc.perform(post("/api/users")
                        .content(objectMapper.writeValueAsString(userRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.name").value(userRequest.name()))
                .andExpect(jsonPath("$.email").value(userRequest.email()))
                .andExpect(jsonPath("$.login").value(userRequest.login()))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.cartId").value("cart-456"));
        //then
        verify(userService, times(1)).registerUser(any(CreateUserRequest.class));
    }

    @Test
    @DisplayName("Test register invalid user functionality")
    void givenInvalidUserToRegister_whenRegister_thenResponseIsBadRequest() throws Exception {
        //given
        CreateUserRequest userRequest = DataUtils.createInvalidUserRequest();

        BDDMockito.given(userService.registerUser(userRequest))
                .willThrow(UserAlreadyExistsException.class);
        //when
        mockMvc.perform(post("/api/users")
                        .content(objectMapper.writeValueAsString(userRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        //then
    }

    @Test
    @DisplayName("Test user find by id functionality")
    void givenIdToFindUser_whenFind_thenSuccessResponse() throws Exception {
        //given
        String userId = "123";
        UserDto userDto = new UserDto(
                userId,
                "John Doe",
                "john.doe@example.com",
                "john_login",
                UserRole.USER,
                LocalDateTime.now(),
                LocalDateTime.now(),
                List.of(),
                List.of(),
                "cart-456"
        );
        BDDMockito.given(userService.getUserById(userId))
                .willReturn(userDto);
        //when
        mockMvc.perform(get("/api/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value(userDto.name()))
                .andExpect(jsonPath("$.email").value(userDto.email()))
                .andExpect(jsonPath("$.login").value(userDto.login()))
                .andExpect(jsonPath("$.cartId").value(userDto.cartId()));
        //then
        BDDMockito.then(userService).should().getUserById(userId);
    }

    @Test
    @DisplayName("Test get user by id throwing exception functinality")
    void givenUserId_whenGetUser_thenExceptionIsThrown() throws Exception {
        //given
        String userId = "345";
        BDDMockito.given(userService.getUserById(userId))
                .willThrow(UserNotFoundException.class);
        //when
        mockMvc.perform(get("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        //then
        BDDMockito.then(userService).should().getUserById(userId);
    }

    @Test
    @DisplayName("Test update user request functionality")
    void givenUserToUpdateRequestAndUserId_whenUpdate_thenSuccessResponse() throws Exception {
        //given
        String userId = "123";
        UpdateUserRequest userRequest = new UpdateUserRequest(
                "John Doe",
                "john.doe@example.com",
                "john_doe_new_password"
        );
        UserDto userDto = new UserDto(
                userId,
                userRequest.name(),
                userRequest.email(),
                "john_login",
                UserRole.USER,
                LocalDateTime.now(),
                LocalDateTime.now(),
                List.of(),
                List.of(),
                "cart-456"
        );
        BDDMockito.given(userService.updateUserById(userRequest, userId))
                .willReturn(userDto);
        //when
        mockMvc.perform(put("/api/users/{id}", userId)
                .content(objectMapper.writeValueAsString(userRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(userRequest.name()))
                .andExpect(jsonPath("$.email").value(userRequest.email()));
        //then
        BDDMockito.then(userService).should().updateUserById(userRequest, userId);
    }

    @Test
    @DisplayName("Test update user throws exception functionality")
    void givenUpdateUserRequestAndUserId_whenUpdate_thenExceptionIsThrown() throws Exception {
        //given
        String userId = "123";
        UpdateUserRequest userRequest = new UpdateUserRequest(
                "John Doe",
                "john.doe@example.com",
                "john_doe_new_password"
        );
        BDDMockito.given(userService.updateUserById(userRequest, userId))
                .willThrow(UserNotFoundException.class);
        //when
        mockMvc.perform(put("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isNotFound());
        //then
        BDDMockito.then(userService).should().updateUserById(userRequest, userId);
    }

    @Test
    @DisplayName("Test role filtering functionality")
    void givenUserRoleToFilter_whenFilter_thenSuccessResponse() throws Exception {
        //given
        UserRole role = UserRole.USER;
        UserDto userDto1 = new UserDto(
                "111",
                "John Doe",
                "john.doe@example.com",
                "john_login",
                UserRole.COURIER,
                LocalDateTime.now(),
                LocalDateTime.now(),
                List.of(),
                List.of(),
                "cart-456"
        );
        UserDto userDto2 = new UserDto(
                "222",
                "Mike Smith",
                "mike.smith@example.com",
                "mike_login",
                role,
                LocalDateTime.now(),
                LocalDateTime.now(),
                List.of(),
                List.of(),
                "cart-567"
        );
        UserDto userDto3 = new UserDto(
                "333",
                "Paul Davis",
                "paul.davis@example.com",
                "paul_login",
                role,
                LocalDateTime.now(),
                LocalDateTime.now(),
                List.of(),
                List.of(),
                "cart-678"
        );

        List<UserDto> filteredUsers = List.of(userDto1, userDto2, userDto3);
        BDDMockito.given(userService.filterByRole(role))
                .willReturn(filteredUsers);
        //when
        mockMvc.perform(get("/api/users")
                        .param("role", role.name())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].role").value(role.name()))
                .andExpect(jsonPath("$[2].role").value(role.name()));
        //then
        BDDMockito.then(userService).should().filterByRole(role);
    }

    @Test
    @DisplayName("Test filter by role throwing exception functionality")
    void givenUserRoleToFilter_whenFilter_thenExceptionIsThrown() throws Exception {
        //given
        BDDMockito.given(userService.filterByRole(null))
                .willThrow(RoleNullableException.class);
        //when
        mockMvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        //then
        BDDMockito.then(userService).should().filterByRole(null);
    }

    @Test
    @DisplayName("Test deleting user functionality")
    void givenUserId_whenDelete_thenSuccessResponse() throws Exception {
        //given
        String userId = "123";
        BDDMockito.doNothing().when(userService)
                .deleteUser(userId);
        //when
        mockMvc.perform(delete("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        //then
        BDDMockito.then(userService).should().deleteUser(userId);
    }

    @Test
    @DisplayName("Test deleting user throwing exception functionality")
    void givenUserId_whenDelete_thenExceptionIsThrown() throws Exception {
        //given
        String userId = "123";
        BDDMockito.willThrow(UserNotFoundException.class)
                .given(userService).deleteUser(userId);
        //when
        mockMvc.perform(delete("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        //then
        BDDMockito.then(userService).should().deleteUser(userId);
    }

    @Test
    @DisplayName("Test finding user history version functionality")
    void givenUserIdAndTime_whenFindingHistoryVersion_thenSuccessResponse() throws Exception {
        //given
        String userId = "123";
        LocalDateTime time = LocalDateTime.now();

        UserHistoryDto userHistory = new UserHistoryDto(
                userId,
                "John Doe",
                "john.doe@example.com",
                "john_login",
                UserRole.USER,
                LocalDateTime.now().minusDays(10),
                LocalDateTime.now()
        );
        BDDMockito.given(userService.findVersion(userId, time))
                .willReturn(userHistory);
        //when
        mockMvc.perform(get("/api/users/{id}/history", userId)
                        .param("param", String.valueOf(time))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.name").value(userHistory.name()))
                .andExpect(jsonPath("$.role").value(userHistory.role().name()));;
        //then
        BDDMockito.then(userService).should().findVersion(userId, time);
    }

    @Test
    @DisplayName("Test finding user history version throwing exception functionality")
    void givenUserIdAndTime_whenFindingHistoryVersion_thenExceptionIsThrown() throws Exception {
        //given
        String userId = "123";
        LocalDateTime time = LocalDateTime.now();

        BDDMockito.given(userService.findVersion(userId, time))
                .willThrow(UserNotFoundException.class);
        //when
        mockMvc.perform(get("/api/users/{id}/history", userId)
                        .param("param", String.valueOf(time))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        //then
        BDDMockito.then(userService).should().findVersion(userId, time);
    }

}