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
import com.egrevs.project.utils.DataUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserHistoryRepository userHistoryRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Test creating user functionality")
    void givenUserToCreate_whenCreateUser_thenRepositoryIsCalled(){
        //given
        CreateUserRequest userRequest = DataUtils.createUserRequest();
        BDDMockito.given(userRepository.existsByLogin(userRequest.login()))
                .willReturn(false);

        BDDMockito.given(userRepository.save(any(User.class)))
                .willAnswer(invocation -> {
                    User u = invocation.getArgument(0);
                    u.setId(UUID.randomUUID().toString());
                    return u;
                });
        //when
        userService.registerUser(userRequest);
        //then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());
        verify(userHistoryRepository, times(1)).save(any(UserHistory.class));

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getName()).isEqualTo(userRequest.name());
        assertThat(savedUser.getEmail()).isEqualTo(userRequest.email());
        assertThat(savedUser.getLogin()).isEqualTo(userRequest.login());
        assertThat(savedUser.getPassword()).isEqualTo(userRequest.password());
    }

    @Test
    @DisplayName("Test login exception when save functionality")
    void givenUserToSave_whenSave_thenExceptionIsThrown(){
        //given
        CreateUserRequest userRequest = DataUtils.createUserRequest();
        BDDMockito.given(userRepository.existsByLogin(userRequest.login()))
                .willReturn(true);
        //when
        assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(userRequest));
        //then
        verify(userRepository, never()).save(any(User.class));
        verify(userHistoryRepository, never()).save(any(UserHistory.class));
    }

    @Test
    @DisplayName("Test getUserById functionality")
    void givenUserToFind_whenFind_thenRepositoryIsCalled(){
        //given
        User user = DataUtils.createUser(UserRole.USER);
        BDDMockito.given(userRepository.findById(user.getId()))
                .willReturn(Optional.of(user));
        //when
        UserDto obtainedUser = userService.getUserById(user.getId());
        //then
        verify(userRepository, times(1)).findById(user.getId());
        assertThat(obtainedUser.id()).isEqualTo(user.getId());
        assertThat(obtainedUser.name()).isEqualTo(user.getName());
        assertThat(obtainedUser.login()).isEqualTo(user.getLogin());
        assertThat(obtainedUser.email()).isEqualTo(user.getEmail());
    }

    @Test
    @DisplayName("Test exception when getting user functionality")
    void givenUserToFind_whenFind_thenExceptionIsThrown(){
        //given
        User user = DataUtils.createUser(UserRole.USER);
        BDDMockito.given(userRepository.findById(user.getId()))
                .willReturn(Optional.empty());
        //when
        //then
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(user.getId()));
    }

    @Test
    @DisplayName("Test updating user functionality")
    void givenUserById_whenUpdate_thenRepositoryIsCalled(){
        //given
        User user = DataUtils.createUser(UserRole.USER);
        BDDMockito.given(userRepository.findById(user.getId()))
                .willReturn(Optional.of(user));

        UpdateUserRequest userRequest = DataUtils.updateUserRequest();
        //when
        BDDMockito.when(userRepository.save(user))
                .thenReturn(user);
        UserDto userDto = userService.updateUserById(userRequest, user.getId());
        //then
        verify(userRepository, times(1)).save(user);
        verify(userHistoryRepository, times(1))
                .save(any(UserHistory.class));

        assertThat(userDto.id()).isEqualTo(user.getId());
        assertThat(userDto.email()).isEqualTo(userRequest.email());
        assertThat(userDto.name()).isEqualTo(userRequest.name());
    }

    @Test
    @DisplayName("Test updating user throwing exception functionality")
    void givenUserById_whenUpdate_thenExceptionIsThrown(){
        //given
        User user = DataUtils.createUser(UserRole.USER);
        BDDMockito.given(userRepository.findById(user.getId()))
                .willReturn(Optional.empty());
        UpdateUserRequest userRequest = DataUtils.updateUserRequest();
        //when
        assertThrows(UserNotFoundException.class, () -> userService.updateUserById(userRequest, user.getId()));
        //then
        verify(userRepository, never()).save(user);
        verify(userHistoryRepository, never()).save(any(UserHistory.class));
    }

    @Test
    @DisplayName("Test deletion of user functionality")
    void givenUserIdToDelete_whenDelete_thenRepositoryIsCalled(){
        //given
        User user = DataUtils.createUser(UserRole.USER);
        BDDMockito.given(userRepository.findById(user.getId()))
                .willReturn(Optional.of(user));
        //when
        userService.deleteUser(user.getId());
        //then
        verify(userRepository, times(1)).delete(user);
        verify(userHistoryRepository, times(1))
                .closeActiveUserVersion(eq(user.getId()), any(LocalDateTime.class));
    }

    @Test
    @DisplayName("Test deletion of user throwing exception functionality")
    void givenUserIdToDelete_whenDelete_thenExceptionIsThrown(){
        //given
        User user = DataUtils.createUser(UserRole.USER);
        BDDMockito.given(userRepository.findById(user.getId()))
                .willReturn(Optional.empty());
        //when
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(user.getId()));
        //then
        verify(userRepository, never()).delete(user);
        verify(userHistoryRepository, never())
                .closeActiveUserVersion(eq(user.getId()), any(LocalDateTime.class));
    }


    @Test
    @DisplayName("Test filtering by role functionality")
    void givenRole_whenFilter_thenRepositoryIsCalled(){
        //given
        UserRole role = UserRole.USER;
        User user1 = DataUtils.createUser(role);
        User user2 = DataUtils.createUser(role);

        BDDMockito.given(userRepository.findAllByRole(role))
                .willReturn(List.of(user1, user2));
        //when
        List<UserDto> result = userService.filterByRole(role);
        //then
        verify(userRepository, times(1)).findAllByRole(role);

        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Test filtering by role throw exception functionality")
    void givenNullRole_whenFilter_thenThrowException() {
        //when + then
        assertThrows(RoleNullableException.class, () -> userService.filterByRole(null));
    }

    @Test
    @DisplayName("findVersion should return UserHistoryDto when version exists")
    void givenUserIdAndTime_whenVersionExists_thenReturnDto() {
        // given
        User user = DataUtils.createUser(UserRole.USER);
        LocalDateTime time = LocalDateTime.now();
        UserHistory userHistory = new UserHistory();
        userHistory.setUserId(user.getId());
        userHistory.setName(user.getName());
        userHistory.setEmail(user.getEmail());
        userHistory.setLogin(user.getLogin());

        BDDMockito.given(userHistoryRepository.findVersionAtTime(user.getId(), time))
                .willReturn(Optional.of(userHistory));
        // when
        UserHistoryDto dto = userService.findVersion(user.getId(), time);
        // then
        verify(userHistoryRepository, times(1))
                .findVersionAtTime(user.getId(), time);

        assertThat(dto).isNotNull();
        assertThat(dto.name()).isEqualTo(userHistory.getName());
    }

}