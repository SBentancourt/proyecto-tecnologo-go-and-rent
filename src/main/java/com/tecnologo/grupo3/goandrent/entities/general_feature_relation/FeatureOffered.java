package com.tecnologo.grupo3.goandrent.entities.general_feature_relation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tecnologo.grupo3.goandrent.entities.Accommodation;
import com.tecnologo.grupo3.goandrent.entities.Feature;
import com.tecnologo.grupo3.goandrent.entities.ServiceType;
import com.tecnologo.grupo3.goandrent.entities.ids.FeatureID;
import com.tecnologo.grupo3.goandrent.entities.ids.ServiceID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
@Table(indexes = {@Index(name = "iFeatureOffered02", columnList = "feature_id")})
public class FeatureOffered {
    @EmbeddedId
    @JsonProperty("id")
    private FeatureID featureID;

    private int value;

    @ManyToOne
    @MapsId("featureId")
    @JoinColumn(name = "feature_id", foreignKey = @ForeignKey(name = "fk_FeatureOffered_Feature_id"))
    private Feature feature;

    @ManyToOne
    @MapsId("accommodationId")
    @JoinColumn(name = "accommodation_id", foreignKey = @ForeignKey(name = "fk_FeatureOffered_Accommodation_id"))
    private Accommodation accommodation;

    public FeatureOffered(int value, Feature feature, Accommodation accommodation) {
        this.featureID = new FeatureID(feature.getId(), accommodation.getId());
        this.value = value;
        this.feature = feature;
        this.accommodation = accommodation;
    }
}
