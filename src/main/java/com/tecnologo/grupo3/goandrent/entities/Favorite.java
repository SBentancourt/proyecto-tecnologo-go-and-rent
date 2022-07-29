package com.tecnologo.grupo3.goandrent.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tecnologo.grupo3.goandrent.entities.ids.FavoriteID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Favorite {
    @EmbeddedId @JsonProperty("id")
    private FavoriteID favoriteID;

    @ManyToOne
    @MapsId("userAlias")
    @JoinColumn(name = "user_alias", referencedColumnName = "alias", insertable = false, updatable = false,
                foreignKey = @ForeignKey(name = "fk_Favorite_User_alias"))
    private User user;

}
