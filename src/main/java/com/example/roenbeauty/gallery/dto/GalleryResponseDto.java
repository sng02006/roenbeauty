package com.example.roenbeauty.gallery.dto;

import com.example.roenbeauty.gallery.entity.Gallery;
import com.example.roenbeauty.gallery.enums.GalleryCategory;

public class GalleryResponseDto {

    private Long id;
    private String imageUrl;
    private String title;
    private String description;
    private GalleryCategory category;
    private Boolean visible;
    private Integer sortOrder;

    public GalleryResponseDto(
            Long id,
            String imageUrl,
            String title,
            String description,
            GalleryCategory category,
            Boolean visible,
            Integer sortOrder
    ) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.title = title;
        this.description = description;
        this.category = category;
        this.visible = visible;
        this.sortOrder = sortOrder;
    }

    public static GalleryResponseDto from(Gallery gallery) {
        return new GalleryResponseDto(
                gallery.getId(),
                gallery.getImageUrl(),
                gallery.getTitle(),
                gallery.getDescription(),
                gallery.getCategory(),
                gallery.getVisible(),
                gallery.getSortOrder()
        );
    }

    public Long getId() {
        return id;
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
}