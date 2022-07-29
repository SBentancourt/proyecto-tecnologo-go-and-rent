package com.tecnologo.grupo3.goandrent.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class BookingsByRegionDTO {
    private int total;
    private int month;
    private int year;
    private String country;
    private String province;
    private String city;
}
