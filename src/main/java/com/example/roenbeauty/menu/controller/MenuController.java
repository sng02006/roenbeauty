package com.example.roenbeauty.menu.controller;

import com.example.roenbeauty.menu.dto.MenuCreateRequestDto;
import com.example.roenbeauty.menu.dto.MenuResponseDto;
import com.example.roenbeauty.menu.dto.MenuUpdateRequestDto;
import com.example.roenbeauty.menu.enums.MenuCategory;
import com.example.roenbeauty.menu.service.MenuService;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/menus")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping
    public MenuResponseDto createMenu(@RequestBody MenuCreateRequestDto requestDto) {
        return menuService.createMenu(requestDto);
    }

    @GetMapping
    public List<MenuResponseDto> getVisibleMenus() {
        return menuService.getVisibleMenus();
    }

    @GetMapping("/category/{category}")
    public List<MenuResponseDto> getMenusByCategory(
            @PathVariable("category") MenuCategory category
    ) {
        return menuService.getMenusByCategory(category);
    }

    @PutMapping("/{id}")
    public MenuResponseDto updateMenu(
            @PathVariable("id") Long id,
            @RequestBody MenuUpdateRequestDto requestDto
    ) {
        return menuService.updateMenu(id, requestDto);
    }

    @GetMapping("/admin")
    public List<MenuResponseDto> getAllMenusForAdmin() {
        return menuService.getAllMenusForAdmin();
    }
}