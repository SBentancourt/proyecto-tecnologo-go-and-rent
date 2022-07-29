package com.tecnologo.grupo3.goandrent.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class LocationDTO {
    private String country;
    private String province;
    private String city;
    private String street;
    private int doorNumber;
    private String coordinates;
}
