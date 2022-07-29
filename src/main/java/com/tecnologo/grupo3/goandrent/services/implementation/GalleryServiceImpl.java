package com.tecnologo.grupo3.goandrent.services.implementation;

import com.tecnologo.grupo3.goandrent.dtos.GalleryDTO;
import com.tecnologo.grupo3.goandrent.entities.Gallery;
import com.tecnologo.grupo3.goandrent.repositories.GalleryRepository;
import com.tecnologo.grupo3.goandrent.services.GalleryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GalleryServiceImpl implements GalleryService {

    @Autowired
    private GalleryRepository galleryRepository;

    @Override
    public Gallery saveGallery(GalleryDTO galleryDTO) {
        return galleryRepository.save(new Gallery(galleryDTO.getName(), galleryDTO.getPhoto(), galleryDTO.getOrderPhoto()));
    }

    @Override
    public void updateGalleryName(int accommodationId, String idAcc) {
        galleryRepository.updateGalleryName(accommodationId, "alojamiento-"+idAcc);
    }
}
