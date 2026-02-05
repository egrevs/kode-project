package com.egrevs.project.domain.repository;

import com.egrevs.project.domain.entity.user.User;
import com.egrevs.project.domain.enums.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Test user saving functionality")
    void givenUserToSave_whenSave_thenUserIsSaved() {
        //given
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setLogin("johndoe");
        user.setPassword("password");
        user.setRole(UserRole.USER);
        //when
        userRepository.save(user);
        //then
        assertTrue(userRepository.existsByLogin("johndoe"));
        assertFalse(userRepository.existsByLogin("nonexistent"));
    }

    @Test
    @DisplayName("Test user role filter functionality")
    void givenThreeUsersToSave_whenFindByUserRole_thenUsersReturned() {
        //given
        User user1 = new User();
        user1.setName("User One");
        user1.setEmail("user1@example.com");
        user1.setLogin("user1");
        user1.setPassword("password1");
        user1.setRole(UserRole.USER);

        User user2 = new User();
        user2.setName("User Two");
        user2.setEmail("user2@example.com");
        user2.setLogin("user2");
        user2.setPassword("password2");
        user2.setRole(UserRole.USER);

        User courier = new User();
        courier.setName("Courier");
        courier.setEmail("courier@example.com");
        courier.setLogin("courier");
        courier.setPassword("password3");
        courier.setRole(UserRole.COURIER);
        //when
        userRepository.saveAll(List.of(user1, user2, courier));
        List<User> users = userRepository.findAllByRole(UserRole.USER);
        //then
        assertThat(users).hasSize(2);
    }

    @Test
    @DisplayName("Test find user by id functionality")
    void givenUserToSave_whenFindById_thenUserIsReturned(){
        //given
        User user = new User();
        user.setName("User One");
        user.setEmail("user1@example.com");
        user.setLogin("user1");
        user.setPassword("password1");
        user.setRole(UserRole.USER);
        //when
        userRepository.save(user);
        User userToFind = userRepository.findById(user.getId()).orElse(null);
        //then
        assertThat(user).isEqualTo(userToFind);
    }

    @Test
    @DisplayName("Test user updating functionality")
    void givenUserToUpdate_whenUpdate_thenUpdatedUserIsReturned(){
        //given
        User user = new User();
        user.setName("User One");
        user.setEmail("user1@example.com");
        user.setLogin("user1");
        user.setPassword("password1");
        user.setRole(UserRole.USER);
        userRepository.save(user);
        //when
        user.setEmail("updatedUser@example.com");
        userRepository.save(user);
        //then
        assertThat(user.getEmail()).isEqualTo("updatedUser@example.com");
    }

    @Test
    @DisplayName("Test user deletion functionality")
    void givenUserToDelete_whenDelete_thenNullIsReturned(){
        //given
        User user = new User();
        user.setName("User One");
        user.setEmail("user1@example.com");
        user.setLogin("user1");
        user.setPassword("password1");
        user.setRole(UserRole.USER);
        userRepository.save(user);
        //when
        userRepository.delete(user);
        //then
        assertThat(userRepository.findById(user.getId())).isEmpty();
    }

}