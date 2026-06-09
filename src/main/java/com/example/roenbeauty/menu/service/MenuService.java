package com.example.roenbeauty.menu.service;

import com.example.roenbeauty.menu.dto.MenuCreateRequestDto;
import com.example.roenbeauty.menu.dto.MenuResponseDto;
import com.example.roenbeauty.menu.dto.MenuUpdateRequestDto;
import com.example.roenbeauty.menu.entity.Menu;
import com.example.roenbeauty.menu.enums.MenuCategory;
import com.example.roenbeauty.menu.repository.MenuRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Transactional
    public MenuResponseDto createMenu(MenuCreateRequestDto requestDto) {
        Integer priceValue = requestDto.getPriceValue();

        String priceText = priceValue == null || priceValue <= 0
                ? ""
                : String.format("%,d", priceValue);

        Menu menu = new Menu(
                requestDto.getName(),
                requestDto.getDescription(),
                priceText,
                priceValue,
                requestDto.getCategory(),
                requestDto.getVisible(),
                requestDto.getSortOrder(),
                requestDto.getDurationMinutes()
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

    @Transactional
    public MenuResponseDto updateMenu(Long id, MenuUpdateRequestDto requestDto) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴입니다."));

        Integer priceValue = requestDto.getPriceValue();

        String priceText = priceValue == null || priceValue <= 0
                ? ""
                : String.format("%,d", priceValue);

        menu.update(
                requestDto.getName(),
                requestDto.getDescription(),
                priceText,
                priceValue,
                requestDto.getCategory(),
                requestDto.getVisible(),
                requestDto.getSortOrder(),
                requestDto.getDurationMinutes()
        );

        return MenuResponseDto.from(menu);
    }

    public List<MenuResponseDto> getAllMenusForAdmin() {
        return menuRepository.findAllByOrderByCategoryAscSortOrderAsc()
                .stream()
                .map(MenuResponseDto::from)
                .toList();
    }
}