package com.example.roenbeauty.gallery.entity;

import com.example.roenbeauty.gallery.enums.GalleryCategory;
import com.example.roenbeauty.global.common.BaseTimeEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "galleries")
public class Gallery extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String imageUrl;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private GalleryCategory category;

    @Column(nullable = false)
    private Boolean visible;

    @Column(nullable = false)
    private Integer sortOrder;

    protected Gallery() {
    }

    public Gallery(
            String imageUrl,
            String title,
            String description,
            GalleryCategory category,
            Boolean visible,
            Integer sortOrder
    ) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.description = description;
        this.category = category;
        this.visible = visible;
        this.sortOrder = sortOrder;
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

    public void update(
            String imageUrl,
            String title,
            String description,
            GalleryCategory category,
            Boolean visible,
            Integer sortOrder
    ) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.description = description;
        this.category = category;
        this.visible = visible;
        this.sortOrder = sortOrder;
    }
}