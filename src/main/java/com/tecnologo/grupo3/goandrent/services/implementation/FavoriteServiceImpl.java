package com.tecnologo.grupo3.goandrent.services.implementation;

import com.tecnologo.grupo3.goandrent.dtos.FavoriteDTO;
import com.tecnologo.grupo3.goandrent.dtos.responses.FavoriteResponse;
import com.tecnologo.grupo3.goandrent.entities.Accommodation;
import com.tecnologo.grupo3.goandrent.entities.Favorite;
import com.tecnologo.grupo3.goandrent.entities.Gallery;
import com.tecnologo.grupo3.goandrent.entities.User;
import com.tecnologo.grupo3.goandrent.entities.ids.FavoriteID;
import com.tecnologo.grupo3.goandrent.repositories.FavoriteRepository;
import com.tecnologo.grupo3.goandrent.repositories.GalleryRepository;
import com.tecnologo.grupo3.goandrent.services.AccommodationService;
import com.tecnologo.grupo3.goandrent.services.FavoriteService;
import com.tecnologo.grupo3.goandrent.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private AccommodationService accommodationService;

    @Autowired
    private UserService userService;

    @Autowired
    private GalleryRepository galleryRepository;

    @Override
    public void addOrDeleteFavorites(FavoriteDTO favoriteDTO) {
        Accommodation accommodation = accommodationService.getAccommodationObject(favoriteDTO.getAccommodation_id());
        FavoriteID favoriteID = new FavoriteID(favoriteDTO.getAlias(), accommodation);
        if (favoriteRepository.findFavoriteByFavoriteID(favoriteID).isPresent()){
            favoriteRepository.deleteById(favoriteID);
        } else {
            User user = userService.getUserByAlias(favoriteID.getUserAlias());
            favoriteRepository.save( new Favorite(favoriteID, user));
        }
    }

    @Override
    public List<FavoriteResponse> getFavorites(String alias) {
        List<Favorite> favorites = favoriteRepository.findFavoritesByFavoriteID_UserAlias(alias);
        List<FavoriteResponse> responseList = new ArrayList<>();
        for (Favorite f: favorites) {
            Accommodation acc = f.getFavoriteID().getAccommodation();
            Float qualification = accommodationService.getAverageReviewAccommodation(acc.getId());
            Gallery gallery = galleryRepository.getFirstPhotoAccommodation(acc.getId(), 1).orElse(null);
            String photo = "";
            if (gallery != null){
                photo = gallery.getName()+"/"+gallery.getPhoto();
            }

            responseList.add(new FavoriteResponse(acc.getId(), acc.getName(), acc.getDescription(), photo, acc.getPrice(), qualification));
        }
        return responseList;
    }
}
