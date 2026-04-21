package com.example.roenbeauty.menu.service;

import com.example.roenbeauty.menu.dto.MenuCreateRequestDto;
import com.example.roenbeauty.menu.dto.MenuResponseDto;
import com.example.roenbeauty.menu.dto.MenuUpdateRequestDto;
import com.example.roenbeauty.menu.entity.Menu;
import com.example.roenbeauty.menu.enums.MenuCategory;
import com.example.roenbeauty.menu.repository.MenuRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MenuService {

    private final MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public MenuResponseDto createMenu(MenuCreateRequestDto requestDto) {
        Menu menu = new Menu(
                requestDto.getName(),
                requestDto.getDescription(),
                requestDto.getPriceText(),
                requestDto.getPriceValue(),
                requestDto.getCategory(),
                requestDto.getVisible(),
                requestDto.getSortOrder()
        );

        Menu savedMenu = menuRepository.save(menu);
        return MenuResponseDto.from(savedMenu);
    }

    public List<MenuResponseDto> getVisibleMenus() {
        return menuRepository.findByVisibleTrueOrderByCategoryAscSortOrderAsc()
                .stream()
                .map(MenuResponseDto::from)
                .toList();
    }

    public List<MenuResponseDto> getMenusByCategory(MenuCategory category) {
        return menuRepository.findByCategoryAndVisibleTrueOrderBySortOrderAsc(category)
                .stream()
                .map(MenuResponseDto::from)
                .toList();
    }

    public MenuResponseDto updateMenu(Long id, MenuUpdateRequestDto requestDto) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴입니다."));

        menu.update(
                requestDto.getName(),
                requestDto.getDescription(),
                requestDto.getPriceText(),
                requestDto.getPriceValue(),
                requestDto.getCategory(),
                requestDto.getVisible(),
                requestDto.getSortOrder()
        );

        return MenuResponseDto.from(menu);
    }
}