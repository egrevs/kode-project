package com.egrevs.project.gateway.repository;

import com.egrevs.project.gateway.entity.User;
import com.egrevs.project.gateway.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByLogin(String login);
    List<User> findAllByRole(UserRole role);

    User findUserById(String id);
}
