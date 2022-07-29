package com.tecnologo.grupo3.goandrent.entities.general_feature_relation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tecnologo.grupo3.goandrent.entities.Accommodation;
import com.tecnologo.grupo3.goandrent.entities.ServiceType;
import com.tecnologo.grupo3.goandrent.entities.ids.ServiceID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
@Table(indexes = {@Index(name = "iServiceOffered02", columnList = "service_id")})
public class ServiceOffered {
    @EmbeddedId
    @JsonProperty("id")
    private ServiceID serviceID;

    private Boolean value;

    @ManyToOne
    @MapsId("serviceId")
    @JoinColumn(name = "service_id", foreignKey = @ForeignKey(name = "fk_ServiceOffered_Service_id"))
    @JsonIgnore
    private ServiceType serviceType;

    @ManyToOne
    @MapsId("accommodationId")
    @JoinColumn(name = "accommodation_id", foreignKey = @ForeignKey(name = "fk_ServiceOffered_Accommodation_id"))
    @JsonIgnore
    private Accommodation accommodation;

    public ServiceOffered(Boolean value, ServiceType serviceType, Accommodation accommodation) {
        this.serviceID = new ServiceID(serviceType.getId(), accommodation.getId());
        this.value = value;
        this.serviceType = serviceType;
        this.accommodation = accommodation;
    }
}
