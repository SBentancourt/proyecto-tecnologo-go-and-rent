package com.tecnologo.grupo3.goandrent.repositories;

import com.tecnologo.grupo3.goandrent.entities.Gallery;
import com.tecnologo.grupo3.goandrent.utils.enums.AccommodationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface GalleryRepository extends JpaRepository<Gallery, Integer> {

    @Query(value = "select * from gallery g where g.accommodation_id = :idAccommodation and g.order_photo = :orderPhoto", nativeQuery = true)
    Optional<Gallery> getFirstPhotoAccommodation(@Param("idAccommodation") int idAccommodation, @Param("orderPhoto") int orderPhoto);

    @Modifying
    @Transactional
    @Query(value = "update gallery g set g.name = :name where g.accommodation_id = :accommodationId", nativeQuery = true)
    void updateGalleryName(@Param("accommodationId") int accommodationId, @Param("name") String name);

    @Modifying
    @Transactional
    @Query(value = "delete from Gallery g where g.name = :name")
    void deleteGalleriesByName(String name);
}
