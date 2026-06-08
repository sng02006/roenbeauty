package com.example.roenbeauty.menu.repository;

import com.example.roenbeauty.menu.entity.Menu;
import com.example.roenbeauty.menu.enums.MenuCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    List<Menu> findByVisibleTrueOrderByCategoryAscSortOrderAsc();

    List<Menu> findByCategoryAndVisibleTrueOrderBySortOrderAsc(MenuCategory category);

    List<Menu> findAllByOrderByCategoryAscSortOrderAsc();
}