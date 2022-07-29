package com.tecnologo.grupo3.goandrent.services;

import com.tecnologo.grupo3.goandrent.dtos.GalleryDTO;
import com.tecnologo.grupo3.goandrent.entities.Gallery;


public interface GalleryService {
    Gallery saveGallery(GalleryDTO galleryDTO);
    void updateGalleryName(int idAccommodation, String idAcc);
}
