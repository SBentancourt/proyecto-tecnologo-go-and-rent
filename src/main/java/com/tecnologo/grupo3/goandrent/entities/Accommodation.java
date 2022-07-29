package com.tecnologo.grupo3.goandrent.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tecnologo.grupo3.goandrent.entities.general_feature_relation.FeatureOffered;
import com.tecnologo.grupo3.goandrent.entities.general_feature_relation.ServiceOffered;
import com.tecnologo.grupo3.goandrent.entities.users_types.Host;
import com.tecnologo.grupo3.goandrent.utils.enums.AccommodationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
//@Data
@AllArgsConstructor @NoArgsConstructor
@Table(indexes = {@Index(name = "iAccommodation02", columnList = "location_id"),
                    @Index(name = "iAccommodation03", columnList = "host_alias")})
public class Accommodation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    private String name;
    private String description;
    private AccommodationStatus status;
    private Float price;
    private Date createdAt;

    @OneToOne
    @Fetch(value = FetchMode.SELECT)
    @JoinColumn(name = "location_id", referencedColumnName = "id",
                foreignKey = @ForeignKey(name = "fk_Accommodation_Location_id"))
    private Location location;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "accommodation_id", foreignKey = @ForeignKey(name = "fk_Gallery_Accommodation_id"))
    private Set<Gallery> gallery;

    @JsonIgnore
    @OneToMany(mappedBy = "accommodation")
    private Set<ServiceOffered> servicesOffered;

    @JsonIgnore
    @OneToMany(mappedBy = "accommodation")
    private Set<FeatureOffered> featuresOffered;

    @ManyToOne
    @JoinColumn(name = "host_alias", foreignKey = @ForeignKey(name = "fk_Accommodation_Host_alias"))
    private Host host;

    public Accommodation(String name, String description, AccommodationStatus status, Float price, Date createdAt, Location location, Set<Gallery> gallery, Host host) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.price = price;
        this.createdAt = createdAt;
        this.location = location;
        this.gallery = gallery;
        this.host = host;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AccommodationStatus getStatus() {
        return status;
    }

    public void setStatus(AccommodationStatus status) {
        this.status = status;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Set<Gallery> getGallery() {
        return gallery;
    }

    public void setGallery(Set<Gallery> gallery) {
        this.gallery = gallery;
    }

    public Set<ServiceOffered> getServicesOffered() {
        return servicesOffered;
    }

    public void setServicesOffered(Set<ServiceOffered> servicesOffered) {
        this.servicesOffered = servicesOffered;
    }

    public Set<FeatureOffered> getFeaturesOffered() {
        return featuresOffered;
    }

    public void setFeaturesOffered(Set<FeatureOffered> featuresOffered) {
        this.featuresOffered = featuresOffered;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }
}
