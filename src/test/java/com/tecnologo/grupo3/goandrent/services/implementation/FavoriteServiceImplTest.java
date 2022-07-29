package com.tecnologo.grupo3.goandrent.services.implementation;

import com.tecnologo.grupo3.goandrent.dtos.FavoriteDTO;
import com.tecnologo.grupo3.goandrent.entities.*;
import com.tecnologo.grupo3.goandrent.entities.ids.FavoriteID;
import com.tecnologo.grupo3.goandrent.entities.users_types.Host;
import com.tecnologo.grupo3.goandrent.repositories.FavoriteRepository;
import com.tecnologo.grupo3.goandrent.repositories.GalleryRepository;
import com.tecnologo.grupo3.goandrent.services.AccommodationService;
import com.tecnologo.grupo3.goandrent.services.UserService;
import com.tecnologo.grupo3.goandrent.utils.enums.AccommodationStatus;
import com.tecnologo.grupo3.goandrent.utils.enums.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class FavoriteServiceImplTest {

    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private AccommodationService accommodationService;
    @Mock
    private UserService userService;
    @Mock
    private GalleryRepository galleryRepository;

    @InjectMocks
    private FavoriteServiceImpl favoriteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addOrDeleteFavorites() {
        when(accommodationService.getAccommodationObject(1)).thenReturn(new Accommodation());
        Optional<Favorite> favorite = Optional.of(new Favorite());
        when(favoriteRepository.findFavoriteByFavoriteID(new FavoriteID("alias", new Accommodation())))
                .thenReturn(favorite);
        FavoriteDTO favoriteDTO = new FavoriteDTO();
        favoriteDTO.setAlias("alias");
        favoriteDTO.setAccommodation_id(1);
        favoriteService.addOrDeleteFavorites(favoriteDTO);
    }

    @Test
    void getFavorites() {
        List<Favorite> favoriteList = new ArrayList<>();
        Set<Gallery> galleries = new HashSet<>();
        Gallery gallery = new Gallery("name", "photo", 1);
        Host host = new Host();
        Location location = new Location("Uruguay", "Canelones", "Solymar", "calle", 1010, "");
        Accommodation accommodation = new Accommodation("name", "description", AccommodationStatus.ACTIVO, Float.parseFloat("10"), new Date(),
                location, galleries, host);
        User user = new User("alias", "", "", "", "", UserStatus.ACEPTADO, new Date(), "", new Date(), "");
        Favorite favorite = new Favorite(new FavoriteID("alias", accommodation), user);
        favoriteList.add(favorite);
        when(favoriteRepository.findFavoritesByFavoriteID_UserAlias("alias")).thenReturn(favoriteList);
        when(accommodationService.getAverageReviewAccommodation(0)).thenReturn(4F);
        when(galleryRepository.getFirstPhotoAccommodation(0, 1)).thenReturn(Optional.of(gallery));
        assertNotNull(favoriteService.getFavorites("alias"));
    }
}