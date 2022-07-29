package com.tecnologo.grupo3.goandrent.dtos.responses;

import com.tecnologo.grupo3.goandrent.dtos.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data @AllArgsConstructor
public class AccommodationInfoResponse {
    private HostInfoDTO host;
    private AccommodationDTO accommodation;
    private LocationDTO location;
    private List<PhotoDTO> photos;
    private List<ServiceValueAccDTO> services;
    private List<FeatureValueAccDTO> features;
    private List<ReviewInfoDTO> reviews;
    private Boolean isFavorite;

    public AccommodationInfoResponse(AccommodationDTO accommodation, LocationDTO location, List<PhotoDTO> photos, List<ServiceValueAccDTO> services, List<FeatureValueAccDTO> features, List<ReviewInfoDTO> reviews) {
        this.accommodation = accommodation;
        this.location = location;
        this.photos = photos;
        this.services = services;
        this.features = features;
        this.reviews = reviews;
    }
}
