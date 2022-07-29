package com.tecnologo.grupo3.goandrent.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class AccommodationsByRegionDTO {
    private int total;
    private String country;
    private String province;
    private String city;
}
