package com.example.roenbeauty.menu.entity;

import com.example.roenbeauty.global.common.BaseTimeEntity;
import com.example.roenbeauty.menu.enums.MenuCategory;
import jakarta.persistence.*;

@Entity
@Table(name = "menus")
public class Menu extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false, length = 50)
    private String priceText;

    @Column
    private Integer priceValue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private MenuCategory category;

    @Column(nullable = false)
    private Boolean visible;

    @Column(nullable = false)
    private Integer sortOrder;

    protected Menu() {
    }

    public Menu(
            String name,
            String description,
            String priceText,
            Integer priceValue,
            MenuCategory category,
            Boolean visible,
            Integer sortOrder
    ) {
        this.name = name;
        this.description = description;
        this.priceText = priceText;
        this.priceValue = priceValue;
        this.category = category;
        this.visible = visible;
        this.sortOrder = sortOrder;
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

    public void update(
            String name,
            String description,
            String priceText,
            Integer priceValue,
            MenuCategory category,
            Boolean visible,
            Integer sortOrder
    ) {
        this.name = name;
        this.description = description;
        this.priceText = priceText;
        this.priceValue = priceValue;
        this.category = category;
        this.visible = visible;
        this.sortOrder = sortOrder;
    }
}