package com.example.roenbeauty.gallery.service;

import com.example.roenbeauty.gallery.dto.GalleryCreateRequestDto;
import com.example.roenbeauty.gallery.dto.GalleryResponseDto;
import com.example.roenbeauty.gallery.dto.GalleryUpdateRequestDto;
import com.example.roenbeauty.gallery.entity.Gallery;
import com.example.roenbeauty.gallery.enums.GalleryCategory;
import com.example.roenbeauty.gallery.repository.GalleryRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GalleryService {

    private final GalleryRepository galleryRepository;

    public GalleryService(GalleryRepository galleryRepository) {
        this.galleryRepository = galleryRepository;
    }

    public GalleryResponseDto createGallery(GalleryCreateRequestDto requestDto) {
        Gallery gallery = new Gallery(
                requestDto.getImageUrl(),
                requestDto.getTitle(),
                requestDto.getDescription(),
                requestDto.getCategory(),
                requestDto.getVisible(),
                requestDto.getSortOrder()
        );

        Gallery savedGallery = galleryRepository.save(gallery);
        return GalleryResponseDto.from(savedGallery);
    }

    public List<GalleryResponseDto> getVisibleGalleries() {
        return galleryRepository.findByVisibleTrueOrderByCategoryAscSortOrderAsc()
                .stream()
                .map(GalleryResponseDto::from)
                .toList();
    }

    public List<GalleryResponseDto> getGalleriesByCategory(GalleryCategory category) {
        return galleryRepository.findByCategoryAndVisibleTrueOrderBySortOrderAsc(category)
                .stream()
                .map(GalleryResponseDto::from)
                .toList();
    }

    public GalleryResponseDto updateGallery(Long id, GalleryUpdateRequestDto requestDto) {
        Gallery gallery = galleryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 갤러리입니다."));

        gallery.update(
                requestDto.getImageUrl(),
                requestDto.getTitle(),
                requestDto.getDescription(),
                requestDto.getCategory(),
                requestDto.getVisible(),
                requestDto.getSortOrder()
        );

        return GalleryResponseDto.from(gallery);
    }

    @Transactional(readOnly = true)
    public List<GalleryResponseDto> getAllGalleries() {
        return galleryRepository.findAllByOrderByCategoryAscSortOrderAsc()
                .stream()
                .map(GalleryResponseDto::from)
                .toList();
    }

    @Transactional
    public void deleteGallery(Long id) {

        Gallery gallery = galleryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 갤러리입니다."));

        galleryRepository.delete(gallery);
    }
}