package com.tecnologo.grupo3.goandrent.services;

import com.tecnologo.grupo3.goandrent.dtos.FavoriteDTO;
import com.tecnologo.grupo3.goandrent.dtos.responses.FavoriteResponse;

import java.util.List;

public interface FavoriteService {
    void addOrDeleteFavorites(FavoriteDTO favoriteDTO);
    List<FavoriteResponse> getFavorites(String alias);
}
