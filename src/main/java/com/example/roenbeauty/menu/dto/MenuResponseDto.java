package com.example.roenbeauty.menu.dto;

import com.example.roenbeauty.menu.entity.Menu;
import com.example.roenbeauty.menu.enums.MenuCategory;

public class MenuResponseDto {

    private Long id;
    private String name;
    private String description;
    private String priceText;
    private Integer priceValue;
    private MenuCategory category;
    private Boolean visible;
    private Integer sortOrder;

    public MenuResponseDto(
            Long id,
            String name,
            String description,
            String priceText,
            Integer priceValue,
            MenuCategory category,
            Boolean visible,
            Integer sortOrder
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.priceText = priceText;
        this.priceValue = priceValue;
        this.category = category;
        this.visible = visible;
        this.sortOrder = sortOrder;
    }

    public static MenuResponseDto from(Menu menu) {
        return new MenuResponseDto(
                menu.getId(),
                menu.getName(),
                menu.getDescription(),
                menu.getPriceText(),
                menu.getPriceValue(),
                menu.getCategory(),
                menu.getVisible(),
                menu.getSortOrder()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPriceText() {
        return priceText;
    }

    public Integer getPriceValue() {
        return priceValue;
    }

    public MenuCategory getCategory() {
        return category;
    }

    public Boolean getVisible() {
        return visible;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }
}