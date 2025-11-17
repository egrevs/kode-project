package com.egrevs.project.domain.repository.restaurant;

import com.egrevs.project.domain.entity.restaurant.MenuItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuItemsRepository extends JpaRepository<MenuItems, String> {
}
