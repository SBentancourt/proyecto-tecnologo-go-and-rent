package com.tecnologo.grupo3.goandrent.entities.ids;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ServiceID implements Serializable {
    @Column(name = "service_id")
    private int serviceId;

    @Column(name = "accommodation_id")
    private int accommodationId;

    public ServiceID(int serviceId, int accommodationId) {
        this.serviceId = serviceId;
        this.accommodationId = accommodationId;
    }

    public ServiceID() {
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
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
        ServiceID serviceID = (ServiceID) o;
        return serviceId == serviceID.serviceId && accommodationId == serviceID.accommodationId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceId, accommodationId);
    }
}
