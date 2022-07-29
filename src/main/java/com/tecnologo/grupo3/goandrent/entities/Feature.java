package com.tecnologo.grupo3.goandrent.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tecnologo.grupo3.goandrent.entities.general_feature_relation.FeatureOffered;
import lombok.Data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
//@Data
@DiscriminatorValue("FEATURE")
public class Feature extends GeneralFeature{
    @JsonIgnore
    @OneToMany(mappedBy = "feature")
    private Set<FeatureOffered> featuresOffered;

    public Feature(int id, String name, Set<FeatureOffered> featuresOffered) {
        super(id, name);
        this.featuresOffered = featuresOffered;
    }

    public Feature(Set<FeatureOffered> featuresOffered) {
        this.featuresOffered = featuresOffered;
    }

    public Feature() {
    }

    public Feature(int id, String name) {
        super(id, name);
    }

    public Set<FeatureOffered> getFeaturesOffered() {
        return featuresOffered;
    }

    public void setFeaturesOffered(Set<FeatureOffered> featuresOffered) {
        this.featuresOffered = featuresOffered;
    }
}
