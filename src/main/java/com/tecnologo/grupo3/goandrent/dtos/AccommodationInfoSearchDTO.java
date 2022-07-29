package com.tecnologo.grupo3.goandrent.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class AccommodationInfoSearchDTO {
    private Integer id;
    private String name;
    private String description;
    private Float price;
    private String photo;
    private Float rating;
}
