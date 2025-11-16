package com.egrevs.project.domain.repository;

import com.egrevs.project.domain.entity.courier.Courier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourierRepository extends JpaRepository<Courier, String> {
    Optional<Courier> findByLogin(String login);
}
