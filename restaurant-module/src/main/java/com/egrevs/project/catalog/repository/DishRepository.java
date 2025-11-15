package com.egrevs.project.catalog.repository;

import com.egrevs.project.catalog.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishRepository extends JpaRepository<Dish, String> {
}
