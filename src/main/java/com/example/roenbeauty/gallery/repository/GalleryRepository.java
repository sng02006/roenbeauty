package com.example.roenbeauty.gallery.repository;

import com.example.roenbeauty.gallery.entity.Gallery;
import com.example.roenbeauty.gallery.enums.GalleryCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GalleryRepository extends JpaRepository<Gallery, Long> {

    List<Gallery> findByVisibleTrueOrderByCategoryAscSortOrderAsc();

    List<Gallery> findByCategoryAndVisibleTrueOrderBySortOrderAsc(GalleryCategory category);

    List<Gallery> findAllByOrderByCategoryAscSortOrderAsc();
}