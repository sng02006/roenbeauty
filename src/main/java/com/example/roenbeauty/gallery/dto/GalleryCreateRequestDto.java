package com.example.roenbeauty.gallery.dto;

import com.example.roenbeauty.gallery.enums.GalleryCategory;

public class GalleryCreateRequestDto {

    private String imageUrl;
    private String title;
    private String description;
    private GalleryCategory category;
    private Boolean visible;
    private Integer sortOrder;

    public GalleryCreateRequestDto() {
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public GalleryCategory getCategory() {
        return category;
    }

    public Boolean getVisible() {
        return visible;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(GalleryCategory category) {
        this.category = category;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }
}