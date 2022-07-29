package com.tecnologo.grupo3.goandrent.entities.ids;

import com.tecnologo.grupo3.goandrent.entities.Accommodation;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FavoriteID implements Serializable {
    @Column(name = "user_alias", nullable = false)
    private String userAlias;

    @ManyToOne
    @JoinColumn(name = "accommodation_id", referencedColumnName = "id", insertable = false, updatable = false,
                foreignKey = @ForeignKey(name = "fk_Favorite_Accommodation_id"))
    private Accommodation accommodation;

    public FavoriteID() {
    }

    public FavoriteID(String userAlias, Accommodation accommodation) {
        this.userAlias = userAlias;
        this.accommodation = accommodation;
    }

    public String getUserAlias() {
        return userAlias;
    }

    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }

    public Accommodation getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FavoriteID that = (FavoriteID) o;
        return Objects.equals(userAlias, that.userAlias) && Objects.equals(accommodation, that.accommodation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userAlias, accommodation);
    }
}
