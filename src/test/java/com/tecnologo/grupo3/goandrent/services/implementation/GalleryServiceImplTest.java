package com.tecnologo.grupo3.goandrent.services.implementation;

import com.tecnologo.grupo3.goandrent.dtos.GalleryDTO;
import com.tecnologo.grupo3.goandrent.entities.Gallery;
import com.tecnologo.grupo3.goandrent.repositories.GalleryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GalleryServiceImplTest {

    @Mock
    private GalleryRepository galleryRepository;

    @InjectMocks
    private GalleryServiceImpl galleryService;

    private Gallery gallery;
    private GalleryDTO galleryDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        gallery = new Gallery("prueba", "prueba", 1);
        galleryDTO = new GalleryDTO("prueba", "prueba", 1);
    }

    @Test
    void saveGallery() {
        when(galleryRepository.save(gallery)).thenReturn(gallery);
        assertNotNull(galleryService.saveGallery(galleryDTO));
    }

    @Test
    void updateGalleryName() {
        doNothing().when(galleryRepository).updateGalleryName(1, "1");
        galleryService.updateGalleryName(1, "1");

    }
}