package com.example.roenbeauty.menu.dto;

import com.example.roenbeauty.menu.enums.MenuCategory;

public class MenuUpdateRequestDto {

    private String name;
    private String description;
    private String priceText;
    private Integer priceValue;
    private MenuCategory category;
    private Boolean visible;
    private Integer sortOrder;

    public MenuUpdateRequestDto() {
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

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriceText(String priceText) {
        this.priceText = priceText;
    }

    public void setPriceValue(Integer priceValue) {
        this.priceValue = priceValue;
    }

    public void setCategory(MenuCategory category) {
        this.category = category;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
}