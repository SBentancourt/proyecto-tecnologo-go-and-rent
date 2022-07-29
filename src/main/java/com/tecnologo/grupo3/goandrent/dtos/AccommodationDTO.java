package com.tecnologo.grupo3.goandrent.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data @AllArgsConstructor
public class AccommodationDTO {
    private String name;
    private String description;
    private Float price;
    private Float qualification;
}
