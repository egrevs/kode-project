package com.egrevs.project.catalog.repository;

import com.egrevs.project.catalog.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Restaurant getById(Long id);

    boolean existsRestaurantByName(String name);
}
