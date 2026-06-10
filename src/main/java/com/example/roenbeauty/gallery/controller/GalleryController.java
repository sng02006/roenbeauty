package com.example.roenbeauty.gallery.controller;

import com.example.roenbeauty.gallery.dto.GalleryCreateRequestDto;
import com.example.roenbeauty.gallery.dto.GalleryResponseDto;
import com.example.roenbeauty.gallery.dto.GalleryUpdateRequestDto;
import com.example.roenbeauty.gallery.enums.GalleryCategory;
import com.example.roenbeauty.gallery.service.GalleryService;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/galleries")
public class GalleryController {

    private final GalleryService galleryService;

    public GalleryController(GalleryService galleryService) {
        this.galleryService = galleryService;
    }

    @PostMapping
    public GalleryResponseDto createGallery(@RequestBody GalleryCreateRequestDto requestDto) {
        return galleryService.createGallery(requestDto);
    }

    @GetMapping
    public List<GalleryResponseDto> getVisibleGalleries() {
        return galleryService.getVisibleGalleries();
    }

    @GetMapping("/category/{category}")
    public List<GalleryResponseDto> getGalleriesByCategory(@PathVariable GalleryCategory category) {
        return galleryService.getGalleriesByCategory(category);
    }

    @PutMapping("/{id}")
    public GalleryResponseDto updateGallery(
            @PathVariable Long id,
            @RequestBody GalleryUpdateRequestDto requestDto
    ) {
        return galleryService.updateGallery(id, requestDto);
    }

    @GetMapping("/admin")
    public List<GalleryResponseDto> getAllGalleries() {
        return galleryService.getAllGalleries();
    }

    @DeleteMapping("/{id}")
    public void deleteGallery(@PathVariable Long id) {
        galleryService.deleteGallery(id);
    }
}