package com.egrevs.project.domain.repository;


import com.egrevs.project.domain.entity.user.User;
import com.egrevs.project.domain.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByLogin(String login);
    List<User> findAllByRole(UserRole role);
}
