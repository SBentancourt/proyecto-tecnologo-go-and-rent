package com.tecnologo.grupo3.goandrent.entities.ids;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FeatureID implements Serializable {
    @Column(name = "feature_id")
    private int featureId;

    @Column(name = "accommodation_id")
    private int accommodationId;

    public FeatureID(int featureId, int accommodationId) {
        this.featureId = featureId;
        this.accommodationId = accommodationId;
    }

    public FeatureID() {
    }

    public int getFeatureId() {
        return featureId;
    }

    public void setFeatureId(int featureId) {
        this.featureId = featureId;
    }

    public int getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(int accommodationId) {
        this.accommodationId = accommodationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeatureID featureID = (FeatureID) o;
        return featureId == featureID.featureId && accommodationId == featureID.accommodationId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(featureId, accommodationId);
    }
}
