package com.tecnologo.grupo3.goandrent.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tecnologo.grupo3.goandrent.entities.general_feature_relation.ServiceOffered;
import lombok.Data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@DiscriminatorValue("SERVICE")
//@Table(name = "service")
public class ServiceType extends GeneralFeature {
    @JsonIgnore
    @OneToMany(mappedBy = "serviceType")
    private Set<ServiceOffered> servicesOffered;

    public ServiceType(int id, String name, Set<ServiceOffered> servicesOffered) {
        super(id, name);
        this.servicesOffered = servicesOffered;
    }

    public ServiceType(Set<ServiceOffered> servicesOffered) {
        this.servicesOffered = servicesOffered;
    }

    public ServiceType() {
    }

    public ServiceType(int id, String name) {
        super(id, name);
    }

    public Set<ServiceOffered> getServicesOffered() {
        return servicesOffered;
    }

    public void setServicesOffered(Set<ServiceOffered> servicesOffered) {
        this.servicesOffered = servicesOffered;
    }
}
