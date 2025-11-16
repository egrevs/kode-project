package com.egrevs.project.domain.repository.restaurant;

import com.egrevs.project.domain.entity.restaurant.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, String> {
    Restaurant getById(String id);

    boolean existsRestaurantByName(String name);
}
