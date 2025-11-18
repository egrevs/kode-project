package com.egrevs.project.domain.repository;

import com.egrevs.project.domain.entity.restaurant.MenuItemsVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuItemVariantsRepository extends JpaRepository<MenuItemsVariant, String> {
}
