package com.tecnologo.grupo3.goandrent.repositories;

import com.tecnologo.grupo3.goandrent.entities.Favorite;
import com.tecnologo.grupo3.goandrent.entities.ids.FavoriteID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, FavoriteID> {
    Optional<Favorite> findFavoriteByFavoriteID(FavoriteID id);
    List<Favorite> findFavoritesByFavoriteID_UserAlias(String alias);

    @Override
    void deleteById(FavoriteID favoriteID);
}
